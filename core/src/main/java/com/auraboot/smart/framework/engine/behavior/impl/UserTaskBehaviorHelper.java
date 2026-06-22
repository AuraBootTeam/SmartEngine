package com.auraboot.smart.framework.engine.behavior.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.auraboot.smart.framework.engine.SmartEngine;
import com.auraboot.smart.framework.engine.configuration.TaskEventPublisher;
import com.auraboot.smart.framework.engine.pvm.event.EventConstant;
import com.auraboot.smart.framework.engine.bpmn.assembly.task.UserTask;
import com.auraboot.smart.framework.engine.common.util.CollectionUtil;
import com.auraboot.smart.framework.engine.common.util.MarkDoneUtil;
import com.auraboot.smart.framework.engine.configuration.IdGenerator;
import com.auraboot.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.auraboot.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.auraboot.smart.framework.engine.constant.TaskInstanceConstant;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.exception.EngineException;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.auraboot.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.auraboot.smart.framework.engine.configuration.VariablePersister;
import com.auraboot.smart.framework.engine.instance.impl.DefaultTaskAssigneeInstance;
import com.auraboot.smart.framework.engine.instance.impl.DefaultVariableInstance;
import com.auraboot.smart.framework.engine.instance.storage.ExecutionInstanceStorage;
import com.auraboot.smart.framework.engine.instance.storage.TaskAssigneeStorage;
import com.auraboot.smart.framework.engine.instance.storage.TaskInstanceStorage;
import com.auraboot.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.auraboot.smart.framework.engine.model.instance.ActivityInstance;
import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;
import com.auraboot.smart.framework.engine.model.instance.InstanceStatus;
import com.auraboot.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskAssigneeInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskInstance;
import com.auraboot.smart.framework.engine.model.instance.VariableInstance;
import com.auraboot.smart.framework.engine.service.param.query.TaskInstanceQueryParam;

/**
 * Created by 高海军 帝奇 74394 on  2020-03-16 00:19.
 */
public class UserTaskBehaviorHelper {

    static List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstances(ExecutionContext context,
                                                                                  UserTask userTask) {
        TaskAssigneeDispatcher taskAssigneeDispatcher = context.getProcessEngineConfiguration()
            .getTaskAssigneeDispatcher();

        if (null == taskAssigneeDispatcher) {
            throw new EngineException("The taskAssigneeService can't be null for UserTask feature");
        }

        return taskAssigneeDispatcher.getTaskAssigneeCandidateInstance(userTask, context);
    }


    /**
     * 查找低优先级的审批人
     * @param taskAssigneeCandidateInstanceList
     * @return
     */
    static List<TaskAssigneeCandidateInstance> findBatchOfHighestPriorityTaskAssigneeList(List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList) {
        if(CollectionUtil.isEmpty(taskAssigneeCandidateInstanceList)) {
            return new ArrayList<TaskAssigneeCandidateInstance>(0);
        }
        int taskAssigneeCandidateInstanceSize = taskAssigneeCandidateInstanceList.size();
        //排序，升序
        if(CollectionUtil.isNotEmpty(taskAssigneeCandidateInstanceList)) {
            Collections.sort(taskAssigneeCandidateInstanceList, new Comparator<TaskAssigneeCandidateInstance>() {
                @Override
                public int compare(TaskAssigneeCandidateInstance one, TaskAssigneeCandidateInstance two) {
                    return  two.getPriority() - one.getPriority() ;
                }
            });
        }
        //优先级低的和所有相同的
        List<TaskAssigneeCandidateInstance> newTaskAssigneeCandidateInstanceList = new ArrayList<TaskAssigneeCandidateInstance>(taskAssigneeCandidateInstanceSize);
        int minPriority = 0;
        for(int i = 0; i < taskAssigneeCandidateInstanceSize; i++) {
            TaskAssigneeCandidateInstance instance = taskAssigneeCandidateInstanceList.get(i);
            if(i == 0) {
                minPriority = instance.getPriority();
                newTaskAssigneeCandidateInstanceList.add(instance);
            }else {
                if(instance.getPriority() == minPriority) {
                    newTaskAssigneeCandidateInstanceList.add(instance);
                }else {
                    break;
                }
            }
        }
        return newTaskAssigneeCandidateInstanceList;
    }

    static void buildTaskAssigneeInstance(TaskAssigneeCandidateInstance taskAssigneeCandidateInstance,
                                           List<TaskAssigneeInstance> taskAssigneeInstanceList,
                                           IdGenerator idGenerator,String tenantId) {
        TaskAssigneeInstance taskAssigneeInstance = new DefaultTaskAssigneeInstance();
        taskAssigneeInstance.setAssigneeId(taskAssigneeCandidateInstance.getAssigneeId());
        taskAssigneeInstance.setAssigneeType(taskAssigneeCandidateInstance.getAssigneeType());
        taskAssigneeInstance.setTenantId(tenantId);

        idGenerator.generate(taskAssigneeInstance);
        taskAssigneeInstanceList.add(taskAssigneeInstance);
    }

    public static void markDoneEIAndCancelTI(ExecutionContext context, ExecutionInstance executionInstance,
                                List<ExecutionInstance> totalExecutionInstanceList, ExecutionInstanceStorage executionInstanceStorage, ProcessEngineConfiguration processEngineConfiguration) {
        // Complete all execution
        for (ExecutionInstance instance : totalExecutionInstanceList) {
            if (instance.isActive()) {
                MarkDoneUtil.markDoneExecutionInstance(instance, executionInstanceStorage,
                    processEngineConfiguration);
            }
        }

        // Find all task
        TaskInstanceStorage taskInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
                ExtensionConstant.COMMON,TaskInstanceStorage.class);

        List<TaskInstance> allTaskInstanceList = queryAllTaskInstanceList(executionInstance, processEngineConfiguration, taskInstanceStorage);

        // Cancel uncompleted task
        for (TaskInstance taskInstance : allTaskInstanceList) {

            //当前的taskInstance 已经在complete方法中更新过了
            if (taskInstance.getExecutionInstanceId().equals(executionInstance.getInstanceId())) {
                continue;
            }

            if (TaskInstanceConstant.COMPLETED.equals(taskInstance.getStatus())) {
                continue;
            }

            // 这里产生了db 读写访问,
            MarkDoneUtil.markDoneTaskInstance(taskInstance,TaskInstanceConstant.CANCELED,taskInstance.getStatus(),context.getRequest(),taskInstanceStorage,processEngineConfiguration);

            // Fire TASK_CANCELED
            TaskEventPublisher publisher = processEngineConfiguration.getTaskEventPublisher();
            if (publisher != null) {
                Map<String, Object> cancelExtra = new HashMap<String, Object>();
                cancelExtra.put("reason", "countersign_decision");
                publisher.publish(EventConstant.TASK_CANCELED, taskInstance,
                        executionInstance.getTenantId(), cancelExtra);
            }
        }
    }



    static void compensateExecutionAndTask(ExecutionContext context, UserTask userTask,
                                           ActivityInstance activityInstance, ExecutionInstance executionInstance,
                                           Map<String, TaskAssigneeCandidateInstance> taskAssigneeMap, ExecutionInstanceStorage executionInstanceStorage,
                                           ExecutionInstanceFactory executionInstanceFactory,
                                           TaskInstanceFactory taskInstanceFactory, ProcessEngineConfiguration processEngineConfiguration) {

        TaskInstanceStorage taskInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
                ExtensionConstant.COMMON,TaskInstanceStorage.class);

        List<TaskInstance> allTaskInstanceList = queryAllTaskInstanceList(executionInstance, processEngineConfiguration, taskInstanceStorage);

        List<String> taskInstanceIdList = new ArrayList<String>();
        if(CollectionUtil.isNotEmpty(allTaskInstanceList)) {
            for(TaskInstance taskInstance : allTaskInstanceList) {
                taskInstanceIdList.add(taskInstance.getInstanceId());
            }
        }
        TaskAssigneeStorage taskAssigneeStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
            ExtensionConstant.COMMON, TaskAssigneeStorage.class);
        Map<String, List<TaskAssigneeInstance>> taskAssigneeInstanceMap = taskAssigneeStorage.findAssigneeOfInstanceList(
                taskInstanceIdList,executionInstance.getTenantId(), processEngineConfiguration);

        if(taskAssigneeInstanceMap != null) {
            for(List<TaskAssigneeInstance> instanceList : taskAssigneeInstanceMap.values()) {
                if(CollectionUtil.isNotEmpty(instanceList)) {
                    for(TaskAssigneeInstance instance : instanceList) {
                        if(taskAssigneeMap.containsKey(instance.getAssigneeId())) {
                            taskAssigneeMap.remove(instance.getAssigneeId());
                        }
                    }
                }
            }
        }
        if(taskAssigneeMap.size() > 0) {
            List<TaskAssigneeCandidateInstance> newTaskAssigneeList = UserTaskBehaviorHelper.findBatchOfHighestPriorityTaskAssigneeList(new ArrayList<TaskAssigneeCandidateInstance>(taskAssigneeMap.values()));
            for (TaskAssigneeCandidateInstance taskAssigneeCandidateInstance : newTaskAssigneeList) {
                ExecutionInstance newExecutionInstance = executionInstanceFactory.create(activityInstance, context);
                executionInstanceStorage.insert(newExecutionInstance, processEngineConfiguration);

                TaskInstance taskInstance = taskInstanceFactory.create(userTask, newExecutionInstance, context);
                taskInstance.setPriority(taskAssigneeCandidateInstance.getPriority());

                //reAssign
                taskInstance = taskInstanceStorage.insert(taskInstance, processEngineConfiguration);

                List<TaskAssigneeInstance> taskAssigneeInstanceList = new ArrayList<TaskAssigneeInstance>(2);
                IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();
                UserTaskBehaviorHelper.buildTaskAssigneeInstance(taskAssigneeCandidateInstance, taskAssigneeInstanceList, idGenerator,activityInstance.getTenantId());
                for(TaskAssigneeInstance taskAssigneeInstance : taskAssigneeInstanceList) {
                    taskAssigneeInstance.setProcessInstanceId(taskInstance.getProcessInstanceId());
                    taskAssigneeInstance.setTaskInstanceId(taskInstance.getInstanceId());
                    taskAssigneeStorage.insert(taskAssigneeInstance, processEngineConfiguration);
                }

                // Fire TASK_ASSIGNED for compensated sequential countersign task
                TaskEventPublisher publisher = processEngineConfiguration.getTaskEventPublisher();
                if (publisher != null) {
                    Map<String, Object> extra = new HashMap<>();
                    List<String> assigneeIds = new ArrayList<>();
                    for (TaskAssigneeInstance tai : taskAssigneeInstanceList) {
                        assigneeIds.add(tai.getAssigneeId());
                    }
                    extra.put("assigneeIds", assigneeIds);
                    publisher.publish(EventConstant.TASK_ASSIGNED, taskInstance,
                            activityInstance.getTenantId(), extra);
                }
            }
        }
    }

     static List<TaskInstance> queryAllTaskInstanceList(ExecutionInstance executionInstance, ProcessEngineConfiguration processEngineConfiguration, TaskInstanceStorage taskInstanceStorage) {
        TaskInstanceQueryParam taskInstanceQueryParam = new TaskInstanceQueryParam();
        List<String> processInstanceIdList = new ArrayList<String>(2);
        processInstanceIdList.add(executionInstance.getProcessInstanceId());
        taskInstanceQueryParam.setProcessInstanceIdList(processInstanceIdList);
        taskInstanceQueryParam.setActivityInstanceId(executionInstance.getActivityInstanceId());
        List<TaskInstance> allTaskInstanceList = taskInstanceStorage.findTaskList(taskInstanceQueryParam,
                processEngineConfiguration);
        return allTaskInstanceList;
    }

    /**
     * Variable key under which a sequential multi-instance userTask caches its full ordered
     * candidate list at enter time. Scoped to the activity instance.
     */
    static final String SEQ_MI_CANDIDATES_KEY = "$mi_seq_candidates$";

    private static final char FIELD_SEP = '\t';
    private static final char ENTRY_SEP = '\n';

    /**
     * Cache the full ordered candidate list of a SEQUENTIAL multi-instance userTask at enter time.
     *
     * <p>Rationale: in sequential countersign the next assignee is only created when the current task
     * completes (a separate transaction), at which point the original command request no longer carries
     * the collection (e.g. {@code smart:miCollection="${approverList}"}). Re-resolving via the dispatcher
     * then yields 0 candidates. Caching the full list at enter and reading it back at complete time fixes
     * this. Scoped by {@code activityInstanceId} so every sequential iteration of the same activity reads
     * the same list.
     *
     * <p>No-op when the candidate list is empty or when variable persistence is not backed by a real
     * store (e.g. the in-memory {@code storage-custom}), in which case callers fall back to the legacy
     * dispatcher re-resolution path. This is engine-internal state, so it must not depend on the
     * business variable persistence enable flag.
     */
    static void cacheSequentialCandidates(ExecutionContext context, ActivityInstance activityInstance,
                                          List<TaskAssigneeCandidateInstance> candidates,
                                          VariableInstanceStorage variableInstanceStorage,
                                          ProcessEngineConfiguration processEngineConfiguration) {
        if (CollectionUtil.isEmpty(candidates) || variableInstanceStorage == null) {
            return;
        }
        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();
        if (variablePersister == null) {
            return;
        }
        VariableInstance variableInstance = new DefaultVariableInstance();
        variableInstance.setTenantId(activityInstance.getTenantId());
        processEngineConfiguration.getIdGenerator().generate(variableInstance);
        variableInstance.setProcessInstanceId(activityInstance.getProcessInstanceId());
        variableInstance.setExecutionInstanceId(activityInstance.getInstanceId());
        variableInstance.setFieldKey(SEQ_MI_CANDIDATES_KEY);
        variableInstance.setFieldType(String.class);
        variableInstance.setFieldValue(serializeCandidates(candidates));
        variableInstanceStorage.insert(variablePersister, variableInstance, processEngineConfiguration);
    }

    /**
     * Load the cached full candidate list of a sequential multi-instance userTask, or {@code null} when
     * none is cached (non-persistent store / not found).
     */
    static List<TaskAssigneeCandidateInstance> loadSequentialCandidates(ActivityInstance activityInstance,
                                          VariableInstanceStorage variableInstanceStorage,
                                          ProcessEngineConfiguration processEngineConfiguration) {
        if (variableInstanceStorage == null) {
            return null;
        }
        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();
        if (variablePersister == null) {
            return null;
        }
        List<VariableInstance> list = variableInstanceStorage.findList(activityInstance.getProcessInstanceId(),
            activityInstance.getInstanceId(), variablePersister, activityInstance.getTenantId(),
            processEngineConfiguration);
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        for (VariableInstance variableInstance : list) {
            if (SEQ_MI_CANDIDATES_KEY.equals(variableInstance.getFieldKey())) {
                Object raw = variableInstance.getFieldValue();
                if (raw == null) {
                    return null;
                }
                return deserializeCandidates(String.valueOf(raw));
            }
        }
        return null;
    }

    private static String serializeCandidates(List<TaskAssigneeCandidateInstance> candidates) {
        StringBuilder sb = new StringBuilder();
        for (TaskAssigneeCandidateInstance candidate : candidates) {
            if (sb.length() > 0) {
                sb.append(ENTRY_SEP);
            }
            // priority<FS>assigneeType<FS>assigneeId — assigneeId last so a (theoretical) separator
            // inside it would still be recovered by the limited split.
            sb.append(candidate.getPriority()).append(FIELD_SEP)
              .append(candidate.getAssigneeType() == null ? "" : candidate.getAssigneeType()).append(FIELD_SEP)
              .append(candidate.getAssigneeId() == null ? "" : candidate.getAssigneeId());
        }
        return sb.toString();
    }

    private static List<TaskAssigneeCandidateInstance> deserializeCandidates(String raw) {
        List<TaskAssigneeCandidateInstance> result = new ArrayList<TaskAssigneeCandidateInstance>();
        if (raw == null || raw.isEmpty()) {
            return result;
        }
        for (String line : raw.split(String.valueOf(ENTRY_SEP))) {
            if (line.isEmpty()) {
                continue;
            }
            String[] parts = line.split(String.valueOf(FIELD_SEP), 3);
            if (parts.length < 3) {
                continue;
            }
            TaskAssigneeCandidateInstance candidate = new TaskAssigneeCandidateInstance();
            try {
                candidate.setPriority(Integer.parseInt(parts[0]));
            } catch (NumberFormatException e) {
                candidate.setPriority(500);
            }
            candidate.setAssigneeType(parts[1]);
            candidate.setAssigneeId(parts[2]);
            result.add(candidate);
        }
        return result;
    }

    public static void abortAndSetNeedPause(ExecutionContext context, ExecutionInstance executionInstance, SmartEngine smartEngine) {
        context.getProcessInstance().setStatus(InstanceStatus.aborted);
        smartEngine.getProcessCommandService().abort(executionInstance.getProcessInstanceId(),
            InstanceStatus.aborted.name(),executionInstance.getTenantId());
        context.setNeedPause(true);
    }

}
