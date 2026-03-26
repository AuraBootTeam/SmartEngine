package com.auraboot.smart.framework.engine.configuration;

import java.util.List;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.model.assembly.Activity;
import com.auraboot.smart.framework.engine.model.instance.TaskAssigneeCandidateInstance;

/**
 * Created by 高海军 帝奇 74394 on 2017 January  11:13.
 */
public interface TaskAssigneeDispatcher {

    List<TaskAssigneeCandidateInstance> getTaskAssigneeCandidateInstance(Activity activity, ExecutionContext context);


}
