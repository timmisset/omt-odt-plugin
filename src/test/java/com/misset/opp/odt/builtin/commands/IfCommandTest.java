package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;

class IfCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        // IF(<boolean>, THEN) => THEN
        assertResolved(IfCommand.INSTANCE, Collections.emptySet(), Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_BOOLEAN_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE));
        // IF(<boolean>, THEN, ELSE) => THEN | ELSE
        assertResolved(IfCommand.INSTANCE, Collections.emptySet(), Set.of(oppModel.XSD_STRING_INSTANCE, oppModel.XSD_INTEGER_INSTANCE), Set.of(oppModel.XSD_BOOLEAN_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_INTEGER_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("IF", IfCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, IfCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, IfCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testSpecificValidationHasErrorWhenCalledWithNonBooleanArgument() {
        PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE));
        ProblemsHolder holder = mock(ProblemsHolder.class);

        IfCommand.INSTANCE.specificValidation(call, holder);
        verify(holder, times(1))
                .registerProblem(call.getCallSignatureArgumentElement(0), TTLValidationUtil.ERROR_MESSAGE_BOOLEAN, ProblemHighlightType.ERROR);
    }

    @Test
    void testSpecificValidationHasNoErrorWhenCalledWithBooleanArgument() {
        PsiCall call = getCall(Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
        ProblemsHolder holder = mock(ProblemsHolder.class);

        IfCommand.INSTANCE.specificValidation(call, holder);
        verify(holder, never())
                .registerProblem(call.getCallSignatureArgumentElement(0), TTLValidationUtil.ERROR_MESSAGE_BOOLEAN, ProblemHighlightType.ERROR);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(IfCommand.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(IfCommand.INSTANCE, 1);
    }
}
