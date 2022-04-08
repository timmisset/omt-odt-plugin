package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

class IndexOfOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IndexOfOperator.INSTANCE, oppModel.XSD_INTEGER_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("INDEX_OF", IndexOfOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, IndexOfOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, IndexOfOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(IndexOfOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(IndexOfOperator.INSTANCE, 1);
    }

    @Test
    void testSpecificValidationHasErrorWhenNoStringInput() {
        PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE));
        doReturn(Set.of(oppModel.XSD_BOOLEAN_INSTANCE)).when(call).resolvePreviousStep();

        IndexOfOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(
                eq(call),
                eq("String required"),
                eq(ProblemHighlightType.ERROR));
    }

    @Test
    void testSpecificValidationHasNoErrorWhenStringInput() {
        PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE));
        doReturn(Set.of(oppModel.XSD_STRING_INSTANCE)).when(call).resolvePreviousStep();

        IndexOfOperator.INSTANCE.validate(call, holder);

        verify(holder, never()).registerProblem(
                eq(call),
                eq("String required"),
                eq(ProblemHighlightType.ERROR));
    }

    @Test
    void testSpecificValidationHasErrorWhenNonStringArgument() {
        PsiCall call = getCall(Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
        doReturn(Set.of(oppModel.XSD_BOOLEAN_INSTANCE)).when(call).resolvePreviousStep();

        IndexOfOperator.INSTANCE.validate(call, holder);

        verify(holder, never()).registerProblem(
                eq(call.getCallSignatureArgumentElement(0)),
                startsWith("String type required"),
                eq(ProblemHighlightType.ERROR));
    }
}
