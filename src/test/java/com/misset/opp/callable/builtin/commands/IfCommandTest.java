package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

class IfCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        // IF(<boolean>, THEN) => THEN
        assertResolved(IfCommand.INSTANCE, Collections.emptySet(), Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_BOOLEAN_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE));
        // IF(<boolean>, THEN, ELSE) => THEN | ELSE
        assertResolved(IfCommand.INSTANCE, Collections.emptySet(), Set.of(oppModel.XSD_STRING_INSTANCE, oppModel.XSD_INTEGER_INSTANCE), Set.of(oppModel.XSD_BOOLEAN_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_INTEGER_INSTANCE));
    }
}
