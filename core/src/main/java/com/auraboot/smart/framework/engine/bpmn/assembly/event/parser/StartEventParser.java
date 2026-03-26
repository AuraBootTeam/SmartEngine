package com.auraboot.smart.framework.engine.bpmn.assembly.event.parser;

import java.util.Map;

import javax.xml.stream.XMLStreamReader;

import com.auraboot.smart.framework.engine.bpmn.assembly.event.StartEvent;
import com.auraboot.smart.framework.engine.bpmn.assembly.process.parser.AbstractBpmnParser;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.xml.parser.ParseContext;
import com.auraboot.smart.framework.engine.xml.util.XmlParseUtil;

@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = StartEvent.class)

public class StartEventParser extends AbstractBpmnParser<StartEvent> {

    @Override
    public Class<StartEvent> getModelType() {
        return StartEvent.class;
    }

    @Override
    public StartEvent parseModel(XMLStreamReader reader, ParseContext context) {
        StartEvent startEvent = new StartEvent();
        startEvent.setId(XmlParseUtil.getString(reader, "id"));
        startEvent.setName(XmlParseUtil.getString(reader, "name"));
        startEvent.setStartActivity(true);

        Map<String, String> properties = super.parseExtendedProperties(reader,  context);
        startEvent.setProperties(properties);
        return startEvent;
    }

}
