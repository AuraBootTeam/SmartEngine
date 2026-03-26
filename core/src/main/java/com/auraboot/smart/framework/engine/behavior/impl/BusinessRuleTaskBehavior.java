package com.auraboot.smart.framework.engine.behavior.impl;

import com.auraboot.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.auraboot.smart.framework.engine.bpmn.assembly.task.BusinessRuleTask;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = BusinessRuleTask.class)
/**
 * @author zilong.jiangzl 2020-07-17
 */
public class BusinessRuleTaskBehavior extends AbstractActivityBehavior<BusinessRuleTask> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessRuleTaskBehavior.class);

    public BusinessRuleTaskBehavior() {
        super();
    }

}
