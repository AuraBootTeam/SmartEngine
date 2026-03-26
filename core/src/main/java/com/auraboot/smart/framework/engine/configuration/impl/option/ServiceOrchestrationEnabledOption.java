package com.auraboot.smart.framework.engine.configuration.impl.option;

import com.auraboot.smart.framework.engine.configuration.ConfigurationOption;


public class ServiceOrchestrationEnabledOption implements ConfigurationOption {

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getId() {
        return "serviceOrchestrationEnabled";
    }

    @Override
    public Object getData() {
        return null;
    }
}