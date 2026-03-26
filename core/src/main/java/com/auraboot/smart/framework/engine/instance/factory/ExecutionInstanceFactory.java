package com.auraboot.smart.framework.engine.instance.factory;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.model.instance.ActivityInstance;
import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;

public interface ExecutionInstanceFactory {

    /**
     * 创建执行实例
     *
     * @return 执行实例
     */
    ExecutionInstance create(ActivityInstance activityInstance, ExecutionContext executionContext);

}
