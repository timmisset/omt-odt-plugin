package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.resolvable.local.LocalVariable;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

class MapCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(MapCommand.INSTANCE);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("MAP", MapCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertTrue(MapCommand.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, MapCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, MapCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetLocalVariablesReturnsLocalVariablesForIndex1() {
        PsiCall call = mock(PsiCall.class);
        List<LocalVariable> localVariables = MapCommand.INSTANCE.getLocalVariables(call, 1);
        Assertions.assertEquals(3, localVariables.size());
        Assertions.assertEquals("$value", localVariables.get(0).getName());
        Assertions.assertEquals("$index", localVariables.get(1).getName());
        Assertions.assertEquals("$array", localVariables.get(2).getName());
    }

    @Test
    void testGetLocalVariablesReturnsNoLocalVariablesForIndexOtherThan1() {
        PsiCall call = mock(PsiCall.class);
        List<LocalVariable> localVariables = MapCommand.INSTANCE.getLocalVariables(call, 0);
        Assertions.assertEquals(0, localVariables.size());
    }
}
