package com.auraboot.smart.framework.engine.delegation;

import com.auraboot.smart.framework.engine.context.ExecutionContext;

/**
 * @author 高海军 帝奇  2016.11.11
 */
public interface JavaDelegation extends RootDelegation {

    void execute(ExecutionContext executionContext);

}
