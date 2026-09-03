package com.auraboot.smart.framework.engine.persister.custom;

import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;
import com.auraboot.smart.framework.engine.model.instance.ActivityInstance;
import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskInstance;
import com.auraboot.smart.framework.engine.persister.custom.session.PersisterSession;

import java.util.List;

/**
 * Session-tree navigation for the CUSTOM (in-memory, per-session) storage
 * bindings. The CUSTOM persister keeps every instance inside the
 * {@link PersisterSession} process-instance tree; these helpers centralize the
 * walks so insert/update/find/remove stay consistent across the four storage
 * classes.
 *
 * @author SmartEngine Team
 */
final class PersisterSessionTreeUtil {

    private PersisterSessionTreeUtil() {
    }

    static ProcessInstance findProcessInstance(PersisterSession session, String processInstanceId) {
        if (session == null || processInstanceId == null) {
            return null;
        }
        return session.getProcessInstances().get(processInstanceId);
    }

    static ProcessInstance findActivityInstanceOwner(PersisterSession session, String activityInstanceId) {
        if (session == null || activityInstanceId == null) {
            return null;
        }
        for (ProcessInstance processInstance : session.getProcessInstances().values()) {
            if (findActivityInstanceIn(processInstance, activityInstanceId) != null) {
                return processInstance;
            }
        }
        return null;
    }

    static ActivityInstance findActivityInstance(PersisterSession session, String activityInstanceId) {
        if (session == null || activityInstanceId == null) {
            return null;
        }
        for (ProcessInstance processInstance : session.getProcessInstances().values()) {
            ActivityInstance match = findActivityInstanceIn(processInstance, activityInstanceId);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    static ActivityInstance findActivityInstanceIn(ProcessInstance processInstance, String activityInstanceId) {
        if (processInstance == null || activityInstanceId == null) {
            return null;
        }
        List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        if (activityInstances == null) {
            return null;
        }
        for (ActivityInstance activityInstance : activityInstances) {
            if (activityInstance != null && activityInstanceId.equals(activityInstance.getInstanceId())) {
                return activityInstance;
            }
        }
        return null;
    }

    static ExecutionInstance findExecutionInstance(PersisterSession session, String executionInstanceId) {
        if (session == null || executionInstanceId == null) {
            return null;
        }
        for (ProcessInstance processInstance : session.getProcessInstances().values()) {
            ExecutionInstance match = findExecutionInstanceIn(processInstance, executionInstanceId);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    static ExecutionInstance findExecutionInstanceIn(ProcessInstance processInstance, String executionInstanceId) {
        if (processInstance == null || executionInstanceId == null) {
            return null;
        }
        List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        if (activityInstances == null) {
            return null;
        }
        for (ActivityInstance activityInstance : activityInstances) {
            List<ExecutionInstance> executionInstances = activityInstance.getExecutionInstanceList();
            if (executionInstances == null) {
                continue;
            }
            for (ExecutionInstance executionInstance : executionInstances) {
                if (executionInstance != null && executionInstanceId.equals(executionInstance.getInstanceId())) {
                    return executionInstance;
                }
            }
        }
        return null;
    }

    static TaskInstance findTaskInstance(PersisterSession session, String taskInstanceId) {
        if (session == null || taskInstanceId == null) {
            return null;
        }
        for (ProcessInstance processInstance : session.getProcessInstances().values()) {
            TaskInstance match = findTaskInstanceIn(processInstance, taskInstanceId);
            if (match != null) {
                return match;
            }
        }
        return null;
    }

    static TaskInstance findTaskInstanceIn(ProcessInstance processInstance, String taskInstanceId) {
        if (processInstance == null || taskInstanceId == null) {
            return null;
        }
        List<ActivityInstance> activityInstances = processInstance.getActivityInstances();
        if (activityInstances == null) {
            return null;
        }
        for (ActivityInstance activityInstance : activityInstances) {
            List<ExecutionInstance> executionInstances = activityInstance.getExecutionInstanceList();
            if (executionInstances == null) {
                continue;
            }
            for (ExecutionInstance executionInstance : executionInstances) {
                if (executionInstance == null || executionInstance.getTaskInstance() == null) {
                    continue;
                }
                TaskInstance taskInstance = executionInstance.getTaskInstance();
                if (taskInstanceId.equals(taskInstance.getInstanceId())) {
                    return taskInstance;
                }
            }
        }
        return null;
    }
}
