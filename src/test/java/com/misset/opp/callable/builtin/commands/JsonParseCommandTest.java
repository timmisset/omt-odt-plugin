package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

class JsonParseCommandTest extends BuiltInTest {
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
}
