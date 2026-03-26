package com.auraboot.smart.framework.engine.instance.factory.impl;

import com.auraboot.smart.framework.engine.common.util.DateUtil;
import com.auraboot.smart.framework.engine.common.util.StringUtil;
import com.auraboot.smart.framework.engine.configuration.IdGenerator;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.instance.factory.ExecutionInstanceFactory;
import com.auraboot.smart.framework.engine.instance.impl.DefaultExecutionInstance;
import com.auraboot.smart.framework.engine.model.instance.ActivityInstance;
import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;


@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = ExecutionInstanceFactory.class)
public class DefaultExecutionInstanceFactory implements ExecutionInstanceFactory {

    @Override
    public ExecutionInstance create(ActivityInstance activityInstance,ExecutionContext executionContext) {
        DefaultExecutionInstance defaultExecutionInstance = new DefaultExecutionInstance();
        IdGenerator idGenerator = executionContext.getProcessEngineConfiguration().getIdGenerator();
        idGenerator.generate(defaultExecutionInstance);
        defaultExecutionInstance.setProcessDefinitionActivityId(activityInstance.getProcessDefinitionActivityId());
        defaultExecutionInstance.setActivityInstanceId(activityInstance.getInstanceId());
        defaultExecutionInstance.setProcessInstanceId(activityInstance.getProcessInstanceId());
        defaultExecutionInstance.setProcessDefinitionIdAndVersion(activityInstance.getProcessDefinitionIdAndVersion());
        defaultExecutionInstance.setStartTime(DateUtil.getCurrentDate());
        defaultExecutionInstance.setActive(true);
        defaultExecutionInstance.setTenantId(executionContext.getTenantId());

        if(StringUtil.isNotEmpty(executionContext.getBlockId())){
            defaultExecutionInstance.setBlockId(executionContext.getBlockId());
        }

        return defaultExecutionInstance;
    }


}
