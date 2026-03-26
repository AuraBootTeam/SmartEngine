package com.auraboot.smart.framework.engine.test.callcactivity2;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.delegation.JavaDelegation;


public class SubDelegation1 implements JavaDelegation {

    @Override
    public void execute(ExecutionContext executionContext) {
        System.out.println("==> SubDelegation1");
    }
}
