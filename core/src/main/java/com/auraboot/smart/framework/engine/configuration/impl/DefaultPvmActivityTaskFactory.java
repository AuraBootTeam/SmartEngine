package com.auraboot.smart.framework.engine.configuration.impl;


import com.auraboot.smart.framework.engine.configuration.PvmActivityTask;
import com.auraboot.smart.framework.engine.configuration.PvmActivityTaskFactory;

public  class DefaultPvmActivityTaskFactory implements PvmActivityTaskFactory {

    public PvmActivityTask create(Object... args){
        return new DefaultPvmActivityTask(args);
    }

}
