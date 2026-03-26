package com.auraboot.smart.framework.engine.pvm;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public interface PvmProcessInstance {

    ProcessInstance start(ExecutionContext executionContext);

    ProcessInstance signal(PvmActivity pvmActivity, ExecutionContext executionContext);

    ProcessInstance jump(PvmActivity pvmActivity, ExecutionContext executionContext);
}
