package com.auraboot.smart.framework.engine.xml.parser;

import com.auraboot.smart.framework.engine.hook.LifeCycleHook;

/**
 * Base interface for artifact parsers. Created by ettear on 16-4-12.
 */
public interface BaseXmlParser<M> extends LifeCycleHook {


    Class<M> getModelType();
    
}
