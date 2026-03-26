package com.auraboot.smart.framework.engine.configuration;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.pvm.PvmActivity;

/**
 * Created by 高海军 帝奇 74394 on  2020-09-21 17:54.
 */
public interface ParallelServiceOrchestration {

    void  orchestrateService(ExecutionContext context, PvmActivity pvmActivity);
}