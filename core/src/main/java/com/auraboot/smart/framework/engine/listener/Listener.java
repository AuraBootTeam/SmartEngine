package com.auraboot.smart.framework.engine.listener;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.pvm.event.EventConstant;

/**
 * Created by 高海军 帝奇 74394 on  2019-09-01 11:51.
 */
public interface Listener {

    void execute(EventConstant event, ExecutionContext executionContext);

}