package com.auraboot.smart.framework.engine.behavior.impl;

import com.auraboot.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.auraboot.smart.framework.engine.bpmn.assembly.task.Task;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = Task.class)
public class TaskBehavior extends AbstractActivityBehavior<Task> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskBehavior.class);




    public TaskBehavior() {
        super();
    }




}