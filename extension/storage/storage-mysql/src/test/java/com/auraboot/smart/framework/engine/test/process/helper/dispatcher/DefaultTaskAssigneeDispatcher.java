package com.auraboot.smart.framework.engine.test.process.helper.dispatcher;

import java.util.ArrayList;
import java.util.List;

import com.auraboot.smart.framework.engine.configuration.TaskAssigneeDispatcher;
import com.auraboot.smart.framework.engine.constant.AssigneeTypeConstant;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.model.assembly.Activity;
import com.auraboot.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  18:03.
 */
public class DefaultTaskAssigneeDispatcher implements TaskAssigneeDispatcher {

    @Override
    public List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstance(Activity activity, ExecutionContext context) {
        List<TaskAssigneeCandidateInstance> taskAssigneeCandidateInstanceList= new ArrayList();

        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance.setAssigneeId("1");
        taskAssigneeCandidateInstance.setAssigneeType(AssigneeTypeConstant.USER);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance);

        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance1 = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance1.setAssigneeId("3");
        taskAssigneeCandidateInstance1.setAssigneeType(AssigneeTypeConstant.USER);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance1);


        TaskAssigneeCandidateInstance taskAssigneeCandidateInstance2 = new TaskAssigneeCandidateInstance();
        taskAssigneeCandidateInstance2.setAssigneeId("5");
        taskAssigneeCandidateInstance2.setAssigneeType(AssigneeTypeConstant.USER);
        taskAssigneeCandidateInstanceList.add(taskAssigneeCandidateInstance2);


        return taskAssigneeCandidateInstanceList;
    }



}
