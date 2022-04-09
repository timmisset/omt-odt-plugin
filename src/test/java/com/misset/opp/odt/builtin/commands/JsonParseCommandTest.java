package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

class JsonParseCommandTest extends BaseBuiltinTest {
    @Override
    @Test
    protected void testResolve() {
        assertResolved(JsonParseCommand.INSTANCE,
                Collections.emptySet(),
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.JSON_OBJECT),
                Set.of(oppModel.XSD_STRING),
                Set.of(oppModel.NAMED_GRAPH));
    }

    @Test
    void testName() {
        Assertions.assertEquals("JSON_PARSE", JsonParseCommand.INSTANCE.getName());
    }

    @Test
    void testDescription() {
        Assertions.assertEquals(JsonParseCommand.DESCRIPTION, JsonParseCommand.INSTANCE.getDescription(""));
    }

    @Test
    void testIsNotVoid() {
        Assertions.assertFalse(JsonParseCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(3, JsonParseCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, JsonParseCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testArgumentTypes() {
        testArgument(JsonParseCommand.INSTANCE, 0, oppModel.JSON_OBJECT, TTLValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(JsonParseCommand.INSTANCE, 1, oppModel.XSD_STRING, TTLValidationUtil.ERROR_MESSAGE_CLASSNAME);
        testArgument(JsonParseCommand.INSTANCE,
                2,
                oppModel.MEDEWERKER_GRAPH,
                TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(JsonParseCommand.INSTANCE, 0, oppModel.JSON_OBJECT);
        assertGetAcceptableArgumentType(JsonParseCommand.INSTANCE, 2, oppModel.NAMED_GRAPH);
        assertGetAcceptableArgumentTypeIsNull(JsonParseCommand.INSTANCE, 1);
    }
}
