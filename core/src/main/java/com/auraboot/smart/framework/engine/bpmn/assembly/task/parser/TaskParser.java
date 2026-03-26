package com.auraboot.smart.framework.engine.bpmn.assembly.task.parser;

import com.auraboot.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.auraboot.smart.framework.engine.bpmn.assembly.task.Task;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.model.assembly.BaseElement;
import com.auraboot.smart.framework.engine.xml.parser.ParseContext;
import com.auraboot.smart.framework.engine.xml.util.XmlParseUtil;

import javax.xml.stream.XMLStreamReader;
import java.util.Map;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Task.class)

public class TaskParser extends AbstractBpmnParser<Task> {

    @Override
    public Class<Task> getModelType() {
        return Task.class;
    }

    @Override
    public Task parseModel(XMLStreamReader reader, ParseContext context) {
        Task serviceTask = new Task();
        serviceTask.setId(XmlParseUtil.getString(reader, "id"));
        serviceTask.setName(XmlParseUtil.getString(reader, "name"));


        Map<String, String> userTaskProperties = super.parseExtendedProperties(reader,  context);
        serviceTask.setProperties(userTaskProperties);

        return serviceTask;
    }

    @Override
    protected boolean parseModelChild(Task model, BaseElement child) {

        return false;
    }

}
