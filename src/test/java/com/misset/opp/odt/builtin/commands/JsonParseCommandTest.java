package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
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
                Set.of(OntologyModelConstants.getXsdStringInstance()),
                Set.of(OntologyModelConstants.getJsonObject()),
                Set.of(OntologyModelConstants.getXsdString()),
                Set.of(OntologyModelConstants.getNamedGraph()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("JSON_PARSE", JsonParseCommand.INSTANCE.getName());
    }

    @Test
    void testDescription() {
        Assertions.assertEquals(JsonParseCommand.DESCRIPTION, JsonParseCommand.INSTANCE.getDescription(null));
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
        testArgument(JsonParseCommand.INSTANCE,
                2,
                OntologyModelConstants.getMedewerkerGraph(),
                OntologyValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(JsonParseCommand.INSTANCE, 0, OntologyModelConstants.getJsonObject());
        assertGetAcceptableArgumentType(JsonParseCommand.INSTANCE, 2, OntologyModelConstants.getNamedGraph());
        assertGetAcceptableArgumentTypeIsNull(JsonParseCommand.INSTANCE, 1);
    }
}
