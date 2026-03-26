package com.auraboot.smart.framework.engine.test.parallelgateway.orchestration.delegation;

import java.util.Map;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.delegation.JavaDelegation;
import com.auraboot.smart.framework.engine.exception.EngineException;

import com.auraboot.smart.framework.engine.persister.common.assistant.pojo.ThreadExecutionResult;
import com.auraboot.smart.framework.engine.test.parallelgateway.single.thread.ServiceTaskDelegation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServiceTaskTimeoutOrchestrationDelegation implements JavaDelegation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceTaskDelegation.class);


    @Override
    public void execute(ExecutionContext executionContext) {

        Map<String, Object> response = executionContext.getResponse();
        try {
            long id = Thread.currentThread().getId();
            // mock business logic
            if (waitTime() > 0) {
                Thread.sleep(waitTime());
                response.put(taskId(), new ThreadExecutionResult(id, waitTime()));
            }
        } catch (InterruptedException e) {
            throw new EngineException(e);
        }

    }

    protected abstract int waitTime();

    protected abstract String taskId();
}
