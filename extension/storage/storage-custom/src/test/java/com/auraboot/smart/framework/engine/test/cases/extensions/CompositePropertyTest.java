package com.auraboot.smart.framework.engine.test.cases.extensions;

import java.util.Map;

import com.auraboot.smart.framework.engine.constant.ExtensionElementsConstant;
import com.auraboot.smart.framework.engine.model.assembly.ExtensionElements;
import com.auraboot.smart.framework.engine.model.assembly.ProcessDefinition;
import com.auraboot.smart.framework.engine.smart.PropertyCompositeKey;
import com.auraboot.smart.framework.engine.smart.PropertyCompositeValue;
import com.auraboot.smart.framework.engine.test.cases.CustomBaseTestCase;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CompositePropertyTest extends CustomBaseTestCase {


    @Test
    public void test() throws Exception {


        ProcessDefinition processDefinition = repositoryCommandService
            .deploy("CompositePropertyTest.bpmn.xml").getFirstProcessDefinition();
        assertEquals(8, processDefinition.getBaseElementList().size());

        ExtensionElements extensionElements = processDefinition.getExtensionElements();
        Map map = (Map)extensionElements.getDecorationMap().get(ExtensionElementsConstant.PROPERTIES);

        PropertyCompositeKey key = new PropertyCompositeKey("json", "key");

        boolean flag = map.containsKey(key);
        Assert.assertTrue(flag);

        PropertyCompositeValue value = (PropertyCompositeValue) map.get(key);
        Assert.assertEquals("{}",value.getAttrMap().get("value"));
    }

}