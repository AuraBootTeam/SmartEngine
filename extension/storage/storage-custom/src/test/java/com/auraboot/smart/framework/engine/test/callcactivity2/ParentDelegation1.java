package com.auraboot.smart.framework.engine.test.callcactivity2;

import com.auraboot.smart.framework.engine.annotation.Retryable;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.delegation.JavaDelegation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ParentDelegation1 implements JavaDelegation {

    @Override
    public void execute(ExecutionContext executionContext) {
        System.out.println("==> ParentDelegation1");
    }
}
