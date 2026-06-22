package com.auraboot.smart.framework.engine.test.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auraboot.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.auraboot.smart.framework.engine.constant.AdHocConstant;
import com.auraboot.smart.framework.engine.constant.AssigneeTypeConstant;
import com.auraboot.smart.framework.engine.constant.RequestMapSpecialKeyConstant;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.model.assembly.Activity;
import com.auraboot.smart.framework.engine.model.assembly.ProcessDefinition;
import com.auraboot.smart.framework.engine.model.instance.InstanceStatus;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskInstance;
import com.auraboot.smart.framework.engine.test.DatabaseBaseTestCase;
import com.auraboot.smart.framework.engine.test.process.helper.CustomExceptioinProcessor;
import com.auraboot.smart.framework.engine.test.process.helper.DefaultMultiInstanceCounter;
import com.auraboot.smart.framework.engine.test.process.helper.DoNothingLockStrategy;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SequentialMultiInstanceCandidateCacheTest extends DatabaseBaseTestCase {

    private static final String APPROVERS_KEY = "seqApprovers";

    @Override
    protected void initProcessConfiguration() {
        super.initProcessConfiguration();
        processEngineConfiguration.setExceptionProcessor(new CustomExceptioinProcessor());
        processEngineConfiguration.setTaskAssigneeDispatcher(new RequestScopedSequentialAssigneeDispatcher());
        processEngineConfiguration.setMultiInstanceCounter(new DefaultMultiInstanceCounter());
        processEngineConfiguration.setLockStrategy(new DoNothingLockStrategy());
    }

    @Test
    public void sequentialMultiInstanceAdvancesAllCandidatesWhenDefaultVariablePersisterIsDisabled() throws Exception {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("sequential-multi-instance-cache-test.bpmn20.xml").getFirstProcessDefinition();

        Map<String, Object> startRequest = new HashMap<String, Object>();
        List<String> approvers = new ArrayList<String>();
        approvers.add("1");
        approvers.add("3");
        approvers.add("5");
        startRequest.put(APPROVERS_KEY, approvers);
        startRequest.put("text", "start");

        ProcessInstance processInstance = processCommandService.start(
            processDefinition.getId(), processDefinition.getVersion(), startRequest);

        assertOnlyPendingTaskForAssignee(processInstance, "1");

        completeOnlyPendingTask(processInstance, "first");
        assertOnlyPendingTaskForAssignee(processInstance, "3");

        completeOnlyPendingTask(processInstance, "second");
        assertOnlyPendingTaskForAssignee(processInstance, "5");

        completeOnlyPendingTask(processInstance, "third");
        ProcessInstance finalProcessInstance = processQueryService.findById(processInstance.getInstanceId());
        Assert.assertEquals(InstanceStatus.completed, finalProcessInstance.getStatus());
        Assert.assertEquals(0, taskQueryService.findAllPendingTaskList(processInstance.getInstanceId()).size());
    }

    private void completeOnlyPendingTask(ProcessInstance processInstance, String text) {
        List<TaskInstance> pendingTasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(1, pendingTasks.size());

        Map<String, Object> approveRequest = new HashMap<String, Object>();
        approveRequest.put(RequestMapSpecialKeyConstant.TASK_INSTANCE_TAG, AdHocConstant.AGREE);
        approveRequest.put("text", text);
        taskCommandService.complete(pendingTasks.get(0).getInstanceId(), approveRequest);
    }

    private void assertOnlyPendingTaskForAssignee(ProcessInstance processInstance, String assigneeId) {
        List<TaskInstance> pendingTasks = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(1, pendingTasks.size());

        List<TaskAssigneeInstance> assignees = taskAssigneeQueryService.findList(pendingTasks.get(0).getInstanceId());
        Assert.assertEquals(1, assignees.size());
        Assert.assertEquals(assigneeId, assignees.get(0).getAssigneeId());
    }

    private static class RequestScopedSequentialAssigneeDispatcher implements TaskAssigneeDispatcher {
        @Override
        public List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstance(Activity activity,
                                                                                     ExecutionContext context) {
            Object rawApprovers = context.getRequest() == null ? null : context.getRequest().get(APPROVERS_KEY);
            if (!(rawApprovers instanceof List)) {
                return new ArrayList<TaskAssigneeCandidateInstance>();
            }

            List approvers = (List) rawApprovers;
            List<TaskAssigneeCandidateInstance> candidates = new ArrayList<TaskAssigneeCandidateInstance>();
            int priority = approvers.size();
            for (Object approver : approvers) {
                TaskAssigneeCandidateInstance candidate = new TaskAssigneeCandidateInstance();
                candidate.setAssigneeId(String.valueOf(approver));
                candidate.setAssigneeType(AssigneeTypeConstant.USER);
                candidate.setPriority(priority--);
                candidates.add(candidate);
            }
            return candidates;
        }
    }
}
