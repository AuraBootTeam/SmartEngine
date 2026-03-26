package com.auraboot.smart.framework.engine.bpmn.assembly.task.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.auraboot.smart.framework.engine.bpmn.assembly.multi.instance.MultiInstanceLoopCharacteristics;
import com.auraboot.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.auraboot.smart.framework.engine.bpmn.assembly.task.UserTask;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.model.assembly.BaseElement;
import com.auraboot.smart.framework.engine.xml.parser.ParseContext;
import com.auraboot.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = UserTask.class)

public class UserTaskParser extends AbstractBpmnParser<UserTask> {

    @Override
    public Class<UserTask> getModelType() {
        return UserTask.class;
    }

    @Override
    public UserTask parseModel(XMLStreamReader reader, ParseContext context) {

        UserTask userTask = new UserTask();
        userTask.setId(XmlParseUtil.getString(reader, "id"));
        userTask.setName(XmlParseUtil.getString(reader, "name"));

        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);

        userTask.setProperties(userTaskProperties);

        return userTask;
    }

    @Override
    protected void decorateChild(UserTask userTask, BaseElement child, ParseContext context) {
        if (child instanceof MultiInstanceLoopCharacteristics) {
            userTask.setMultiInstanceLoopCharacteristics((MultiInstanceLoopCharacteristics) child);
        }else{
            super.decorateChild(userTask,child, context);
        }
    }


}
