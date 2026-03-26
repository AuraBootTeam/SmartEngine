package com.auraboot.smart.framework.engine.instance.factory;

import java.util.Map;

import com.auraboot.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;

/**
 * 流程实例工厂 Created by ettear on 16-4-20.
 */
public interface ProcessInstanceFactory {

    /**
     * 创建流程实例
     *
     * @return 流程实例
     */
    ProcessInstance create(ProcessEngineConfiguration processEngineConfiguration, String processDefinitionId, String processDefinitionVersion, Map<String, Object> request);


    ProcessInstance createChild(ProcessEngineConfiguration processEngineConfiguration, String processDefinitionId, String processDefinitionVersion, Map<String, Object> request,String parentInstanceId, String parentExecutionInstanceId);

}
