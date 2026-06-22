package com.auraboot.smart.framework.engine.test.service;

import java.util.List;

import com.auraboot.smart.framework.engine.constant.AssigneeTypeConstant;
import com.auraboot.smart.framework.engine.model.assembly.ProcessDefinition;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskInstance;
import com.auraboot.smart.framework.engine.persister.database.dao.AssigneeOperationRecordDAO;
import com.auraboot.smart.framework.engine.persister.database.dao.RollbackRecordDAO;
import com.auraboot.smart.framework.engine.persister.database.entity.AssigneeOperationRecordEntity;
import com.auraboot.smart.framework.engine.persister.database.entity.RollbackRecordEntity;
import com.auraboot.smart.framework.engine.test.DatabaseBaseTestCase;

import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration("/spring/application-test.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class TaskCommandServiceOperatorUserTest extends DatabaseBaseTestCase {

    @Setter(onMethod = @__({@Autowired}))
    private AssigneeOperationRecordDAO assigneeOperationRecordDAO;

    @Setter(onMethod = @__({@Autowired}))
    private RollbackRecordDAO rollbackRecordDAO;

    @Test
    public void explicitOperatorIsRecordedForAddAndRemoveAssigneeOnUnclaimedTask() throws Exception {
        ProcessInstance processInstance = startSingleUserTaskProcess();
        TaskInstance taskInstance = findOnlyPendingTask(processInstance);
        Assert.assertNull(taskInstance.getClaimUserId());

        TaskAssigneeCandidateInstance candidate = createUserCandidate("operator-user-target");
        taskCommandService.addTaskAssigneeCandidateWithReason(
            taskInstance.getInstanceId(), null, candidate, "need another reviewer", "operator-add");

        taskCommandService.removeTaskAssigneeCandidateWithReason(
            taskInstance.getInstanceId(), null, candidate, "reviewer no longer needed", "operator-remove");

        List<AssigneeOperationRecordEntity> records = assigneeOperationRecordDAO
            .selectByTaskInstanceId(Long.valueOf(taskInstance.getInstanceId()), null);

        AssigneeOperationRecordEntity addRecord = findAssigneeOperationRecord(records, "add_assignee");
        Assert.assertEquals("operator-add", addRecord.getOperatorUserId());
        Assert.assertEquals("operator-user-target", addRecord.getTargetUserId());

        AssigneeOperationRecordEntity removeRecord = findAssigneeOperationRecord(records, "remove_assignee");
        Assert.assertEquals("operator-remove", removeRecord.getOperatorUserId());
        Assert.assertEquals("operator-user-target", removeRecord.getTargetUserId());
    }

    @Test
    public void explicitOperatorIsRecordedForRollbackOnUnclaimedTask() throws Exception {
        ProcessInstance processInstance = startSingleUserTaskProcess();
        TaskInstance taskInstance = findOnlyPendingTask(processInstance);
        Assert.assertNull(taskInstance.getClaimUserId());

        taskCommandService.rollbackTask(
            taskInstance.getInstanceId(), "userTask1", "rollback for correction", "operator-rollback", null);

        List<RollbackRecordEntity> records = rollbackRecordDAO
            .selectByProcessInstanceId(Long.valueOf(processInstance.getInstanceId()), null);

        Assert.assertEquals(1, records.size());
        Assert.assertEquals("operator-rollback", records.get(0).getOperatorUserId());
        Assert.assertEquals("userTask1", records.get(0).getFromActivityId());
        Assert.assertEquals("userTask1", records.get(0).getToActivityId());
    }

    private ProcessInstance startSingleUserTaskProcess() throws Exception {
        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("user-task-id-and-group-test.bpmn20.xml").getFirstProcessDefinition();
        return processCommandService.start(processDefinition.getId(), processDefinition.getVersion());
    }

    private TaskInstance findOnlyPendingTask(ProcessInstance processInstance) {
        List<TaskInstance> taskInstances = taskQueryService.findAllPendingTaskList(processInstance.getInstanceId());
        Assert.assertEquals(1, taskInstances.size());
        return taskInstances.get(0);
    }

    private TaskAssigneeCandidateInstance createUserCandidate(String userId) {
        TaskAssigneeCandidateInstance candidate = new TaskAssigneeCandidateInstance();
        candidate.setAssigneeId(userId);
        candidate.setAssigneeType(AssigneeTypeConstant.USER);
        return candidate;
    }

    private AssigneeOperationRecordEntity findAssigneeOperationRecord(List<AssigneeOperationRecordEntity> records,
                                                                      String operationType) {
        for (AssigneeOperationRecordEntity record : records) {
            if (operationType.equals(record.getOperationType())) {
                return record;
            }
        }
        Assert.fail("No assignee operation record found for " + operationType);
        return null;
    }
}
