package com.auraboot.smart.framework.engine.test.delegation;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.listener.Listener;
import com.auraboot.smart.framework.engine.pvm.event.EventConstant;

/**
 * @author ettear
 * Created by ettear on 06/08/2017.
 */
public class TccTracker implements Listener {


    @Override
    public void execute(EventConstant event,
                        ExecutionContext executionContext) {
        String text = (String)executionContext.getRequest().get("text");

        executionContext.getResponse().put("hello1",text);

    }


}
