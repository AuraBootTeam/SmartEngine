package com.auraboot.smart.framework.engine.test.parallelgateway.orchestration.delegation;

import java.util.Map;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.delegation.JavaDelegation;
import com.auraboot.smart.framework.engine.exception.EngineException;

import com.auraboot.smart.framework.engine.persister.common.assistant.pojo.ThreadExecutionResult;
import com.auraboot.smart.framework.engine.test.parallelgateway.single.thread.ServiceTaskDelegation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AServiceTaskOrchestrationDelegation implements JavaDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskDelegation.class);


    @Override
    public void execute(ExecutionContext executionContext) {

        Map<String, Object> request = executionContext.getRequest();
        String processDefinitionActivityId = executionContext.getExecutionInstance().getProcessDefinitionActivityId();
        Long sleepTime = (Long)request.get( processDefinitionActivityId);

        long id = Thread.currentThread().getId();

        request.put(processDefinitionActivityId,new ThreadExecutionResult(id,sleepTime));

        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new EngineException(e);
        }

    }
}
