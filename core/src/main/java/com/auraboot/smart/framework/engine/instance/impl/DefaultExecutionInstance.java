package com.auraboot.smart.framework.engine.instance.impl;

import com.auraboot.smart.framework.engine.model.instance.ExecutionInstance;
import com.auraboot.smart.framework.engine.model.instance.TaskInstance;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class DefaultExecutionInstance extends AbstractLifeCycleInstance implements ExecutionInstance {

    private static final long serialVersionUID = 2323809298485587299L;
    private String processDefinitionIdAndVersion;
    private String processInstanceId;
    private String activityInstanceId;

    private String processDefinitionActivityId;
    private boolean active;

    private String blockId;

    private TaskInstance taskInstance;

    @Override
    public String toString() {
        return this.getInstanceId()+":"+this.getProcessDefinitionActivityId()+":"+this.isActive();
    }
}
