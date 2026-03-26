package com.auraboot.smart.framework.engine.instance.factory.impl;

import java.util.Map;

import com.auraboot.smart.framework.engine.common.util.DateUtil;
import com.auraboot.smart.framework.engine.configuration.IdGenerator;
import com.auraboot.smart.framework.engine.constant.TaskInstanceConstant;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.instance.factory.TaskInstanceFactory;
import com.auraboot.smart.framework.engine.instance.impl.DefaultTaskInstance;
import com.auraboot.smart.framework.engine.model.assembly.Activity;
import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskInstance;

import  com.auraboot.smart.framework.engine.common.util.InstanceUtil;

/**
 * 默认任务实例工厂实现 Created by ettear on 16-4-20.
 */
@ExtensionBinding(group = ExtensionConstant.COMMON, bindKey = TaskInstanceFactory.class)
public class DefaultTaskInstanceFactory implements TaskInstanceFactory {

    @Override
    public TaskInstance create(Activity activity, ExecutionInstance executionInstance, ExecutionContext context) {
        TaskInstance taskInstance = new DefaultTaskInstance();

        IdGenerator idGenerator = context.getProcessEngineConfiguration().getIdGenerator();

        idGenerator.generate(taskInstance);
        taskInstance.setProcessInstanceId(executionInstance.getProcessInstanceId());
        taskInstance.setActivityInstanceId(executionInstance.getActivityInstanceId());
        taskInstance.setProcessDefinitionActivityId(activity.getId());
        taskInstance.setExecutionInstanceId(executionInstance.getInstanceId());
        taskInstance.setProcessDefinitionIdAndVersion(executionInstance.getProcessDefinitionIdAndVersion());
        taskInstance.setStartTime(DateUtil.getCurrentDate());
        taskInstance.setStatus(TaskInstanceConstant.PENDING);
        taskInstance.setTenantId(context.getTenantId());

        ProcessInstance processInstance = context.getProcessInstance();
        taskInstance.setProcessDefinitionType(processInstance.getProcessDefinitionType());

        Map<String, Object> request = context.getRequest();

        InstanceUtil.enrich(request, taskInstance);

        return taskInstance;
    }


}
