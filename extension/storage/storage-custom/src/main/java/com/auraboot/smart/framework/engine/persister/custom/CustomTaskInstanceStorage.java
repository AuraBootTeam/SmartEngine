package com.auraboot.smart.framework.engine.persister.custom;

import java.util.List;

import com.auraboot.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskInstance;
import com.auraboot.smart.framework.engine.persister.custom.session.PersisterSession;
import com.auraboot.smart.framework.engine.service.param.query.PendingTaskQueryParam;
import com.auraboot.smart.framework.engine.service.param.query.TaskInstanceQueryByAssigneeParam;
import com.auraboot.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

/**
 * CUSTOM (in-memory, per-session) task storage.
 *
 * <p>Tasks live on their owning execution inside the {@link PersisterSession}
 * process-instance tree. Insert/update/remove reconcile the task onto that
 * tree; find walks it. Multi-instance helpers are not backed yet — automation
 * flows that need them require the persister to grow those lookups first.
 *
 * @author SmartEngine Team
 */
@ExtensionBinding(group = ExtensionConstant.CUSTOM, bindKey = TaskInstanceStorage.class)

public class CustomTaskInstanceStorage implements TaskInstanceStorage {

    @Override
    public List<TaskInstance> findTaskByProcessInstanceIdAndStatus(TaskInstanceQueryParam taskInstanceQueryParam,
                                                                   ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public List<TaskInstance> findPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                                  ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Long countPendingTaskList(PendingTaskQueryParam pendingTaskQueryParam,
                                     ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public List<TaskInstance> findTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                                     ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Long countTaskListByAssignee(TaskInstanceQueryByAssigneeParam param,
                                        ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public List<TaskInstance> findTaskList(TaskInstanceQueryParam taskInstanceQueryParam,
                                           ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public Long count(TaskInstanceQueryParam taskInstanceQueryParam,
                      ProcessEngineConfiguration processEngineConfiguration) {
        return null;
    }

    @Override
    public TaskInstance insert(TaskInstance instance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession session = PersisterSession.currentSession();
        ExecutionInstance owner = PersisterSessionTreeUtil.findExecutionInstance(
            session, instance.getExecutionInstanceId());
        if (owner != null) {
            owner.setTaskInstance(instance);
        }
        // No owning execution in this session: adopt the instance as-is so the
        // caller keeps its identity; it becomes visible once its execution is.
        return instance;
    }

    @Override
    public TaskInstance update(TaskInstance instance,
                               ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession session = PersisterSession.currentSession();
        ExecutionInstance owner = PersisterSessionTreeUtil.findExecutionInstance(
            session, instance.getExecutionInstanceId());
        if (owner != null) {
            // The owning execution is reachable: point the tree at the
            // caller's authoritative object.
            owner.setTaskInstance(instance);
            return instance;
        }
        TaskInstance stored = PersisterSessionTreeUtil.findTaskInstance(
            session, instance.getInstanceId());
        if (stored != null) {
            // Detached view only: reflect the caller's status onto it.
            stored.setStatus(instance.getStatus());
            return stored;
        }
        return insert(instance, processEngineConfiguration);
    }

    @Override
    public int updateFromStatus(TaskInstance taskInstance, String fromStatus,
                                ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession session = PersisterSession.currentSession();
        TaskInstance stored = PersisterSessionTreeUtil.findTaskInstance(
            session, taskInstance.getInstanceId());
        if (stored == null || !fromStatus.equals(stored.getStatus())) {
            return 0;
        }
        stored.setStatus(taskInstance.getStatus());
        return 1;
    }

    @Override
    public TaskInstance find(String instanceId, String tenantId,
                             ProcessEngineConfiguration processEngineConfiguration) {
        return PersisterSessionTreeUtil.findTaskInstance(PersisterSession.currentSession(), instanceId);
    }

    @Override
    public void remove(String instanceId, String tenantId,
                       ProcessEngineConfiguration processEngineConfiguration) {
        PersisterSession session = PersisterSession.currentSession();
        for (ProcessInstance processInstance : session.getProcessInstances().values()) {
            List<com.auraboot.smart.framework.engine.model.instance.ActivityInstance> activityInstances =
                processInstance.getActivityInstances();
            if (activityInstances == null) {
                continue;
            }
            for (com.auraboot.smart.framework.engine.model.instance.ActivityInstance activityInstance : activityInstances) {
                List<ExecutionInstance> executionInstances = activityInstance.getExecutionInstanceList();
                if (executionInstances == null) {
                    continue;
                }
                for (ExecutionInstance executionInstance : executionInstances) {
                    if (executionInstance != null && executionInstance.getTaskInstance() != null
                        && instanceId.equals(executionInstance.getTaskInstance().getInstanceId())) {
                        executionInstance.setTaskInstance(null);
                        return;
                    }
                }
            }
        }
    }
}
