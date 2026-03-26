package com.auraboot.smart.framework.engine.pvm;

import com.auraboot.smart.framework.engine.behavior.TransitionBehavior;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.model.assembly.Transition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmTransition extends PvmElement<Transition> {

    PvmActivity getSource();

    PvmActivity getTarget();

    boolean match(ExecutionContext context);
    //
    //void execute(ExecutionContext context);

    void setBehavior(TransitionBehavior transitionBehavior);

}
