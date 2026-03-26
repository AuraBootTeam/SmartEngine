package com.auraboot.smart.framework.engine.bpmn.behavior.gateway;

import java.util.Map;

import com.auraboot.smart.framework.engine.behavior.base.AbstractActivityBehavior;
import com.auraboot.smart.framework.engine.bpmn.assembly.gateway.ExclusiveGateway;
import com.auraboot.smart.framework.engine.bpmn.behavior.gateway.helper.CommonGatewayHelper;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.exception.EngineException;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.pvm.PvmActivity;
import com.auraboot.smart.framework.engine.pvm.PvmTransition;
import com.auraboot.smart.framework.engine.pvm.event.EventConstant;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = ExclusiveGateway.class)
public class ExclusiveGatewayBehavior extends AbstractActivityBehavior<ExclusiveGateway> {

    public ExclusiveGatewayBehavior() {
        super();
    }

    @Override
    public void leave(ExecutionContext context, PvmActivity pvmActivity) {

        fireEvent(context,pvmActivity, EventConstant.ACTIVITY_END);


        //执行每个节点的hook方法
        Map<String, PvmTransition> outcomeTransitions = pvmActivity.getOutcomeTransitions();


            if( outcomeTransitions.size() >=2){

                CommonGatewayHelper.chooseOnlyOne(  pvmActivity ,context);

            }else {
                throw new EngineException("the outcomeTransitions.size() should >= 2");
            }
        }



}
