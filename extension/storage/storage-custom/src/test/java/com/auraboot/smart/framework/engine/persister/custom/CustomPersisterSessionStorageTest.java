package com.auraboot.smart.framework.engine.persister.custom;

import com.auraboot.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.auraboot.smart.framework.engine.instance.impl.DefaultActivityInstance;
import com.auraboot.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.auraboot.smart.framework.engine.instance.impl.DefaultProcessInstance;
import com.auraboot.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.auraboot.smart.framework.engine.model.instance.ActivityInstance;
import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskInstance;
import com.auraboot.smart.framework.engine.persister.custom.session.PersisterSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the CUSTOM (per-session) storage reconcile semantics that
 * back automation synthesized flows: tasks hang off their owning execution in
 * the PersisterSession tree, and insert/update/find/updateFromStatus/remove
 * operate on that tree instead of throwing.
 *
 * @author SmartEngine Team
 */
public class CustomPersisterSessionStorageTest {

    // The storages never touch the engine configuration; the parameter only
    // exists on the storage SPI. Null keeps the fixture free of engine boot.
    private static final ProcessEngineConfiguration CONFIG = null;

    private CustomActivityInstanceStorage activityStorage = new CustomActivityInstanceStorage();
    private CustomExecutionInstanceStorage executionStorage = new CustomExecutionInstanceStorage();
    private CustomTaskInstanceStorage taskStorage = new CustomTaskInstanceStorage();

    private ProcessInstance processInstance;
    private ActivityInstance activityInstance;
    private ExecutionInstance executionInstance;

    @Before
    public void setUp() {
        PersisterSession.create();

        processInstance = new DefaultProcessInstance();
        processInstance.setInstanceId("pi-1");
        processInstance.setTenantId("t-1");

        activityInstance = new DefaultActivityInstance();
        activityInstance.setInstanceId("ai-1");
        activityInstance.setProcessInstanceId("pi-1");
        activityInstance.setProcessDefinitionActivityId("userTask-1");

        executionInstance = new DefaultExecutionInstance();
        executionInstance.setInstanceId("ei-1");
        executionInstance.setProcessInstanceId("pi-1");
        executionInstance.setActivityInstanceId("ai-1");
        executionInstance.setProcessDefinitionActivityId("userTask-1");
        executionInstance.setActive(true);

        java.util.List<ExecutionInstance> executions =
            new java.util.ArrayList<ExecutionInstance>();
        executions.add(executionInstance);
        activityInstance.setExecutionInstanceList(executions);

        processInstance.getActivityInstances().add(activityInstance);

        // The process storage registers the root instance into the session map;
        // the fixture replays that step.
        PersisterSession.currentSession().getProcessInstances().put("pi-1", processInstance);
    }

    @After
    public void tearDown() {
        PersisterSession.destroySession();
    }

    private TaskInstance newTask(String id, String status) {
        TaskInstance taskInstance = new DefaultTaskInstance();
        taskInstance.setInstanceId(id);
        taskInstance.setActivityInstanceId("ai-1");
        taskInstance.setExecutionInstanceId("ei-1");
        taskInstance.setProcessInstanceId("pi-1");
        taskInstance.setStatus(status);
        return taskInstance;
    }

    @Test
    public void findWalksTheSessionTree() {
        PersisterSessionTreeUtil.findExecutionInstance(PersisterSession.currentSession(), "ei-1")
            .setTaskInstance(newTask("task-1", "pending"));

        assertSame("task-1", taskStorage.find("task-1", "t-1", CONFIG).getInstanceId());
        assertNull(taskStorage.find("task-missing", "t-1", CONFIG));
    }

    @Test
    public void insertAttachesTaskToItsOwningExecution() {
        TaskInstance inserted = taskStorage.insert(newTask("task-1", "pending"), CONFIG);

        assertSame(inserted, executionInstance.getTaskInstance());
        assertSame(inserted, taskStorage.find("task-1", "t-1", CONFIG));
    }

    @Test
    public void updateReplacesForeignTaskObjectInTheTree() {
        taskStorage.insert(newTask("task-1", "pending"), CONFIG);

        TaskInstance foreign = newTask("task-1", "canceled");
        TaskInstance updated = taskStorage.update(foreign, CONFIG);

        assertSame("tree view must point at the caller's authoritative object",
            foreign, executionInstance.getTaskInstance());
        assertSame(foreign, updated);
    }

    @Test
    public void updateFromStatusAppliesCasTransition() {
        taskStorage.insert(newTask("task-1", "pending"), CONFIG);

        TaskInstance casTarget = newTask("task-1", "canceled");
        assertEquals(1, taskStorage.updateFromStatus(casTarget, "pending", CONFIG));
        assertEquals("canceled", taskStorage.find("task-1", "t-1", CONFIG).getStatus());
        assertEquals("from-status mismatch must not transition", 0,
            taskStorage.updateFromStatus(casTarget, "pending", CONFIG));
    }

    @Test
    public void removeDetachesTaskFromItsExecution() {
        taskStorage.insert(newTask("task-1", "pending"), CONFIG);

        taskStorage.remove("task-1", "t-1", CONFIG);

        assertNull(executionInstance.getTaskInstance());
        assertNull(taskStorage.find("task-1", "t-1", CONFIG));
    }

    @Test
    public void activityUpdateReplacesForeignObjectAndFindWithShadingScopesByProcess() {
        ActivityInstance foreign = new DefaultActivityInstance();
        foreign.setInstanceId("ai-1");
        foreign.setProcessInstanceId("pi-1");
        foreign.setProcessDefinitionActivityId("userTask-1");

        ActivityInstance updated = activityStorage.update(foreign, CONFIG);

        assertSame(foreign, updated);
        assertSame(foreign, activityStorage.findWithShading("pi-1", "ai-1", "t-1", CONFIG));
        assertNull(activityStorage.findWithShading("pi-other", "ai-1", "t-1", CONFIG));
    }

    @Test
    public void activityRemoveDetachesFromTheProcessTree() {
        activityStorage.remove("ai-1", "t-1", CONFIG);

        assertNull(PersisterSessionTreeUtil.findActivityInstance(
            PersisterSession.currentSession(), "ai-1"));
        assertNull(executionStorage.findWithShading("pi-1", "ei-1", "t-1", CONFIG));
    }

    @Test
    public void executionRemoveDetachesFromItsActivity() {
        executionStorage.remove("ei-1", "t-1", CONFIG);

        assertNull(PersisterSessionTreeUtil.findExecutionInstance(
            PersisterSession.currentSession(), "ei-1"));
        assertNull(executionStorage.findWithShading("pi-1", "ei-1", "t-1", CONFIG));
    }
}
