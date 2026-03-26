package com.auraboot.smart.framework.engine.pvm.impl;

import java.util.Map;

import com.auraboot.smart.framework.engine.model.assembly.ProcessDefinition;
import com.auraboot.smart.framework.engine.pvm.PvmActivity;
import com.auraboot.smart.framework.engine.pvm.PvmProcessDefinition;
import com.auraboot.smart.framework.engine.pvm.PvmTransition;

import lombok.Data;

/**
 * @author 高海军 帝奇  2016.11.11
 * @author ettear 2016.04.13
 */
@Data
public class DefaultPvmProcessDefinition implements PvmProcessDefinition {

    private String idAndVersion;

    private String id;

    private String version;

    private String tenantId;

    private Map<String, PvmActivity> activities;

    private Map<String, PvmTransition> transitions;

    private PvmActivity startActivity;

    private ProcessDefinition model;


}
