package com.auraboot.smart.framework.engine.test;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.listener.Listener;
import com.auraboot.smart.framework.engine.pvm.event.EventConstant;
import com.auraboot.smart.framework.engine.test.cases.extensions.MultiValueAndHelloListenerTest;

public class HelloListener implements Listener {
    @Override
    public void execute(EventConstant event,
                        ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("hello");
        MultiValueAndHelloListenerTest.trace.add(text);
    }
}
