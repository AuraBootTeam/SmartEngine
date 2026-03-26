package com.auraboot.smart.framework.engine.test.delegation;

import java.util.HashMap;
import java.util.Map;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.delegation.JavaDelegation;

public class ExclusiveGatewayDelegation implements JavaDelegation {

    public void execute(ExecutionContext executionContext) {
        Map<String, Object> request = executionContext.getRequest();

        if(null == request){
            request = new HashMap<String, Object>();
            executionContext.setRequest(request);

        }

        request.put("route", "a");

    }

}
