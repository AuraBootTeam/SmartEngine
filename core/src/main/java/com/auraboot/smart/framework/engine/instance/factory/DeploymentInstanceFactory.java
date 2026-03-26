package com.auraboot.smart.framework.engine.instance.factory;

import com.auraboot.smart.framework.engine.model.instance.DeploymentInstance;
import com.auraboot.smart.framework.engine.service.param.command.CreateDeploymentCommand;

/**
 * 流程实例工厂 Created by ettear on 16-4-20.
 */

public interface DeploymentInstanceFactory {

    /**
     * 创建流程实例
     *
     * @return 流程实例
     */
    DeploymentInstance  create(CreateDeploymentCommand createDeploymentCommand);


}
