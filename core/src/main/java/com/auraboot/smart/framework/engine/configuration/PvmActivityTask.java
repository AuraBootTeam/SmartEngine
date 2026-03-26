package com.auraboot.smart.framework.engine.configuration;


import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.pvm.PvmActivity;

import java.util.concurrent.Callable;

public  interface PvmActivityTask extends Callable<ExecutionContext> {


}
