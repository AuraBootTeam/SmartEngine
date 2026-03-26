package com.auraboot.smart.framework.engine.bpmn.behavior.process;

import com.auraboot.smart.framework.engine.behavior.TransitionBehavior;
import com.auraboot.smart.framework.engine.behavior.base.AbstractTransitionBehavior;
import com.auraboot.smart.framework.engine.bpmn.assembly.process.SequenceFlow;
import com.auraboot.smart.framework.engine.common.expression.ExpressionUtil;
import com.auraboot.smart.framework.engine.context.ExecutionContext;
import com.auraboot.smart.framework.engine.exception.EngineException;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.model.assembly.ConditionExpression;
import com.auraboot.smart.framework.engine.model.assembly.Transition;

@ExtensionBinding(group = ExtensionConstant.ACTIVITY_BEHAVIOR, bindKey = TransitionBehavior.class)
public class SequenceFlowBehavior extends AbstractTransitionBehavior<SequenceFlow> {

    @Override
    public boolean match(ExecutionContext context, Transition transition) {

        ConditionExpression conditionExpression = transition.getConditionExpression();

        if (null != conditionExpression) {
            return ExpressionUtil.eval(context, conditionExpression);
        }else{
            throw new EngineException("Should config condition expression for ExclusiveGateway,the sequenceFlow is "+transition);
        }
    }
}
