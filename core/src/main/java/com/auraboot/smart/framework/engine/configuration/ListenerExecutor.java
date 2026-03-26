package com.auraboot.smart.framework.engine.configuration;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.model.assembly.ExtensionElementContainer;
import com.auraboot.smart.framework.engine.pvm.event.EventConstant;

/**
 * Created by 高海军 帝奇 74394 on 2020 August  17:42.
 */
public interface ListenerExecutor {

    void execute(EventConstant event, ExtensionElementContainer extensionElementContainer, ExecutionContext context);

}
