package com.auraboot.smart.framework.engine.test.delegation;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.delegation.JavaDelegation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiValueAndEventListenerDelegation implements JavaDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiValueAndEventListenerDelegation.class);

    private static final AtomicLong counter = new AtomicLong();

    public static Long getCounter() {
        return counter.get();
    }

    public void execute(ExecutionContext executionContext) {
        Map<String, Object> request = executionContext.getRequest();
        LOGGER.info("Delegation executing: invoke some hsf code stuff" + request);
        counter.addAndGet(1);

        //Assert.assertEquals("value1",request.get("key1"));
        //Assert.assertEquals("value2",request.get("key2"));

    }

}
