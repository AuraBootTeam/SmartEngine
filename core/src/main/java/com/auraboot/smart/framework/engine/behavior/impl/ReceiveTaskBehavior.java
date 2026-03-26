package com.auraboot.smart.framework.engine.behavior.impl;

import com.auraboot.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.auraboot.smart.framework.engine.bpmn.assembly.task.ReceiveTask;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.pvm.PvmActivity;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ReceiveTask.class)
public class ReceiveTaskBehavior extends AbstractActivityBehavior<ReceiveTask> {

    public ReceiveTaskBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        super.enter(context, pvmActivity);

        return true;
    }

}
