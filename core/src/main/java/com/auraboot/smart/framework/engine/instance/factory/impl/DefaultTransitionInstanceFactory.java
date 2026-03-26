package com.auraboot.smart.framework.engine.instance.factory.impl;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.instance.factory.TransitionInstanceFactory;
import com.auraboot.smart.framework.engine.instance.impl.DefaultTransitionInstance;
import com.auraboot.smart.framework.engine.model.instance.TransitionInstance;

/**
 * 默认关联实例工厂实现 Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TransitionInstanceFactory.class)

public class DefaultTransitionInstanceFactory implements TransitionInstanceFactory {

    @Override
    public TransitionInstance create(ExecutionContext executionContext) {
        return new DefaultTransitionInstance();
    }
}
