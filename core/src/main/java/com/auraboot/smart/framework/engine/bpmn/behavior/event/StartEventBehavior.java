package com.auraboot.smart.framework.engine.bpmn.behavior.event;

import com.auraboot.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.auraboot.smart.framework.engine.bpmn.assembly.event.StartEvent;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.pvm.PvmActivity;
import com.auraboot.smart.framework.engine.pvm.event.EventConstant;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = StartEvent.class)

public class StartEventBehavior extends AbstractActivityBehavior<StartEvent> {


    public StartEventBehavior() {
        super();
    }

    @Override
    public boolean enter(ExecutionContext context, PvmActivity pvmActivity) {

        fireEvent(context, pvmActivity, EventConstant.PROCESS_START);

        return super.enter(context, pvmActivity);
    }
}
