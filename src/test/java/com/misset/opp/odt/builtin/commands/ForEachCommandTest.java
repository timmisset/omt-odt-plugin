package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

class ForEachCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsVoid(ForEachCommand.INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FOREACH", ForEachCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, ForEachCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, ForEachCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetLocalVariablesReturnsLocalVariablesForIndex1() {
        PsiCall call = mock(PsiCall.class);
        List<Variable> localVariables = ForEachCommand.INSTANCE.getLocalVariables(call, 1);
        Assertions.assertEquals(3, localVariables.size());
        Assertions.assertEquals("$value", localVariables.get(0).getName());
        Assertions.assertEquals("$index", localVariables.get(1).getName());
        Assertions.assertEquals("$array", localVariables.get(2).getName());
    }

    @Test
    void testGetLocalVariablesReturnsNoLocalVariablesForIndexOtherThan1() {
        PsiCall call = mock(PsiCall.class);
        List<Variable> localVariables = ForEachCommand.INSTANCE.getLocalVariables(call, 0);
        Assertions.assertEquals(0, localVariables.size());
    }
}
