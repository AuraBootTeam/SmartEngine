package com.auraboot.smart.framework.engine.bpmn.assembly.common.parser;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import com.auraboot.smart.framework.engine.bpmn.assembly.common.Outgoing;
import com.auraboot.smart.framework.engine.extension.annotation.ExtensionBinding;
import com.auraboot.smart.framework.engine.extension.constant.ExtensionConstant;
import com.auraboot.smart.framework.engine.xml.parser.AbstractElementParser;
import com.auraboot.smart.framework.engine.xml.parser.ParseContext;
import com.auraboot.smart.framework.engine.xml.util.XmlParseUtil;

/**
 * Created by 高海军 帝奇 74394 on 2017 August  10:02.
 */
@ExtensionBinding(group = ExtensionConstant.ELEMENT_PARSER, bindKey = Outgoing.class)

public class OutgoingParser extends AbstractElementParser<Outgoing>  {

    @Override
    public Class<Outgoing> getModelType() {
        return Outgoing.class;
    }

    @Override
    public Outgoing parseElement(XMLStreamReader reader, ParseContext context) throws XMLStreamException {
        // JUST SKIP
        XmlParseUtil.skipToEndElement(reader);

        return null;
    }

}
