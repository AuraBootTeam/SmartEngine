package com.auraboot.smart.framework.engine.behavior.base;

import com.auraboot.smart.framework.engine.behavior.TransitionBehavior;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.model.assembly.Transition;
import com.auraboot.smart.framework.engine.pvm.PvmTransition;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public abstract class AbstractTransitionBehavior<T extends Transition> implements TransitionBehavior {

    private PvmTransition runtimeTransition;



    @Override
    public boolean match(ExecutionContext context, Transition model) {
        return false;
    }



    protected T getModel() {
        return (T)this.runtimeTransition.getModel();
    }
}
