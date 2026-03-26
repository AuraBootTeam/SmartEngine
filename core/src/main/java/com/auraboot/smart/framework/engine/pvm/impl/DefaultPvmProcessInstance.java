package com.auraboot.smart.framework.engine.pvm.impl;

import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.deployment.ProcessDefinitionContainer;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.model.assembly.ProcessDefinition;
import com.auraboot.smart.framework.engine.model.instance.ProcessInstance;
import com.auraboot.smart.framework.engine.pvm.PvmActivity;
import com.auraboot.smart.framework.engine.pvm.PvmProcessDefinition;
import com.auraboot.smart.framework.engine.pvm.PvmProcessInstance;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
public class DefaultPvmProcessInstance implements PvmProcessInstance {

    @Override
    public ProcessInstance start(ExecutionContext executionContext) {

        ProcessDefinition processDefinition = executionContext.getProcessDefinition();

        ProcessDefinitionContainer processDefinitionContainer = executionContext
            .getProcessEngineConfiguration().getAnnotationScanner().getExtensionPoint(ExtensionConstant.SERVICE,
                ProcessDefinitionContainer.class);

        PvmProcessDefinition pvmProcessDefinition = processDefinitionContainer.getPvmProcessDefinition(processDefinition.getId(),
                processDefinition.getVersion(),processDefinition.getTenantId());

        PvmActivity pvmActivity = pvmProcessDefinition.getStartActivity();

        return this.jump(pvmActivity,executionContext);

    }


    @Override
    public ProcessInstance signal(PvmActivity pvmActivity, ExecutionContext executionContext) {

        //execute
        pvmActivity.execute( executionContext);

        ProcessInstance processInstance =  executionContext.getProcessInstance();
        return processInstance;
    }

    @Override
    public ProcessInstance jump(PvmActivity pvmActivity, ExecutionContext executionContext) {

        //enter
        pvmActivity.enter(executionContext);

        ProcessInstance processInstance =  executionContext.getProcessInstance();
        return processInstance;

    }

}
