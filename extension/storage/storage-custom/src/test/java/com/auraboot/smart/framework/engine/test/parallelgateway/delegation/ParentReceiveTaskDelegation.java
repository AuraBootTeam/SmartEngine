package com.auraboot.smart.framework.engine.test.parallelgateway.delegation;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.delegation.JavaDelegation;

public class ParentReceiveTaskDelegation implements JavaDelegation {

    @Override
    public void execute(ExecutionContext executionContext) {
        ChildServiceTaskDelegation.counter.addAndGet(7);
        System.out.println("ParentReceiveTaskDelegation");

    }



}
