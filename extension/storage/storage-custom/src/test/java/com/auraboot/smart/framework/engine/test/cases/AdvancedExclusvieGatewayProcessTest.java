package com.auraboot.smart.framework.engine.test.cases;

import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;
import com.auraboot.smart.framework.engine.test.delegation.BasicServiceTaskDelegation;
import com.auraboot.smart.framework.engine.test.delegation.ExclusiveTaskDelegation;

import org.junit.Test;

public class AdvancedExclusvieGatewayProcessTest extends CommonTestCode {

    @Override
    public void setUp() {
        super.setUp();

        BasicServiceTaskDelegation.resetCounter();
        ExclusiveTaskDelegation.resetCounter();
    }

    @Test
    public void test() throws Exception {

        ExecutionInstance executionInstance = commonCodeSnippet("advanced-exclusviegateway-process.bpmn.xml");


        commonCode(null, executionInstance);

    }

}