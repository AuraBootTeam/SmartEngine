package com.auraboot.smart.framework.engine.service.command.impl;

import com.auraboot.smart.framework.engine.configuration.ProcessEngineConfiguration;
import com.auraboot.smart.framework.engine.configuration.VariablePersister;
import com.auraboot.smart.framework.engine.configuration.aware.ProcessEngineConfigurationAware;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.hook.LifeCycleHook;
import com.auraboot.smart.framework.engine.instance.storage.VariableInstanceStorage;
import com.auraboot.smart.framework.engine.model.instance.VariableInstance;
import com.auraboot.smart.framework.engine.service.command.VariableCommandService;

/**
 * 主要变量插入。
 *
 * @author 高海军 帝奇  2021.02.25
 */
@ExtensionBinding(group = ExtensionConstant.SERVICE, bindKey = VariableCommandService.class)
public class DefaultVariableCommandService   implements VariableCommandService , LifeCycleHook, ProcessEngineConfigurationAware {

    private  ProcessEngineConfiguration processEngineConfiguration;

    @Override
    public void insert(VariableInstance... variableInstances) {

        VariablePersister variablePersister = processEngineConfiguration.getVariablePersister();

        for (VariableInstance instance : variableInstances) {
            variableInstanceStorage.insert(variablePersister,instance,processEngineConfiguration);
        }

    }


    @Override
    public void start() {

        this.variableInstanceStorage = processEngineConfiguration.getAnnotationScanner().getExtensionPoint(
            ExtensionConstant.COMMON, VariableInstanceStorage.class);

    }


    @Override
    public void stop() {

    }



    private VariableInstanceStorage variableInstanceStorage;
    @Override
    public void setProcessEngineConfiguration(ProcessEngineConfiguration processEngineConfiguration) {
        this.processEngineConfiguration = processEngineConfiguration;
    }


}
