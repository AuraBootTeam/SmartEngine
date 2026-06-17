package com.auraboot.smart.framework.engine.service.command;

import java.util.Map;

import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskInstance;

/**
 * 主要负责人工任务处理服务。
 *
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface TaskCommandService {

    ProcessInstance complete(String taskId, Map<String, Object> request);

    /**
     * Claim a task by setting the claimUserId. Only unclaimed tasks can be claimed.
     */
    void claim(String taskId, String userId, String tenantId);

    ProcessInstance complete(String taskId, String userId, Map<String, Object> request);

    ProcessInstance complete(String taskId, Map<String, Object> request, Map<String, Object> response);

    /**
     * 任务转交。
     */
    void transfer(String taskId, String fromUserId, String toUserId);
    void transfer(String taskId, String fromUserId, String toUserId,String tenantId);

    /**
     * 创建任务实例. 这个相当于是数据订正，一般不需要使用。
     */
    TaskInstance createTask(ExecutionInstance executionInstance, String taskInstanceStatus, Map<String, Object> request);

    /**
     * 将任务实例标记完成. 这个相当于是数据订正，一般不需要使用。
     */
    void markDone(String taskId, Map<String, Object> request);

    /**
     * 删除任务的处理者. 这个相当于是数据订正，一般不需要使用。
     */
    void removeTaskAssigneeCandidate(String taskId,String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance);

    /**
     * 增加任务的处理者. 这个相当于是数据订正，一般不需要使用。
     */
    void addTaskAssigneeCandidate(String taskId,String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance);

    /**
     * 增强的任务移交，支持原因和时限
     */
    void transferWithReason(String taskId, String fromUserId, String toUserId, String reason, String tenantId);

    /**
     * 任务回退到指定节点
     */
    ProcessInstance rollbackTask(String taskId, String targetActivityId, String reason, String tenantId);

    /**
     * 任务回退到指定节点（显式操作人）。
     *
     * <p>{@code operatorUserId} 记录到 se_process_rollback_record.operator_user_id（NOT NULL）。
     * 旧的四参重载从任务的 claimUserId 推断操作人，但任务仅被分派而未被认领时
     * claimUserId 为 null，会触发非空约束违反。调用方应传入真实操作人。
     * {@code operatorUserId} 为 null 时回退到 claimUserId（向后兼容）。
     */
    ProcessInstance rollbackTask(String taskId, String targetActivityId, String reason, String operatorUserId, String tenantId);

    /**
     * 增强的加签操作，支持操作记录
     */
    void addTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason);

    /**
     * 增强的加签操作，支持操作记录与显式操作人。
     *
     * <p>{@code operatorUserId} 记录到 se_assignee_operation_record.operator_user_id（NOT NULL）。
     * 为 null 时回退到任务 claimUserId（向后兼容）。调用方应传入真实操作人，
     * 避免任务未认领时操作人为 null 触发非空约束违反。
     */
    void addTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason, String operatorUserId);

    /**
     * 增强的减签操作，支持操作记录
     */
    void removeTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason);

    /**
     * 增强的减签操作，支持操作记录与显式操作人。
     *
     * <p>语义同 {@link #addTaskAssigneeCandidateWithReason} 的操作人重载。
     */
    void removeTaskAssigneeCandidateWithReason(String taskId, String tenantId, TaskAssigneeCandidateInstance taskAssigneeCandidateInstance, String reason, String operatorUserId);

}
