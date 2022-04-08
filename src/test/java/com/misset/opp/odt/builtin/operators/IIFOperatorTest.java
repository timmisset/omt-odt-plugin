package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class IIFOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IIfOperator.INSTANCE,
                Collections.emptySet(),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE, oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    protected void testResolveSingleArgument() {
        assertResolved(IIfOperator.INSTANCE,
                Collections.emptySet(),
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE));
    }

    @Test
    void testName() {
        Assertions.assertEquals("IIF", IIfOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, IIfOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, IIfOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(IIfOperator.INSTANCE, 0, oppModel.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeSameAsArgument(IIfOperator.INSTANCE, 1, 2);
        assertGetAcceptableArgumentTypeSameAsArgument(IIfOperator.INSTANCE, 2, 1);
        assertGetAcceptableArgumentTypeIsNull(IIfOperator.INSTANCE, 3);
    }

    @Test
    void testSpecificValidationHasErrorWhenNoBooleanAtIndex0() {
        PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE));
        IIfOperator.INSTANCE.validate(call, holder);
        verify(holder).registerProblem(
                eq(call.getCallSignatureArgumentElement(0)),
                startsWith("Boolean type required"),
                eq(ProblemHighlightType.ERROR));
    }

    @Test
    void testSpecificValidationHasNoErrorWhenBooleanAtIndex0() {
        PsiCall call = getCall(Set.of(oppModel.XSD_BOOLEAN_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE));
        IIfOperator.INSTANCE.validate(call, holder);
        verify(holder, never()).registerProblem(
                eq(call.getCallSignatureArgumentElement(0)),
                startsWith("Boolean type required"),
                eq(ProblemHighlightType.ERROR));
    }

    @Test
    void testSpecificValidationHasWarningWhenIncompatibleTypes() {
        PsiCall call = getCall(Set.of(oppModel.XSD_BOOLEAN_INSTANCE), Set.of(oppModel.XSD_BOOLEAN_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE));
        IIfOperator.INSTANCE.validate(call, holder);
        verify(holder)
                .registerProblem(call, "Possible outcomes are incompatible, not illegal but it smells fishy", ProblemHighlightType.WEAK_WARNING);
    }

    @Test
    void testSpecificValidationHasNoWarningWhenNoIncompatibleTypes() {
        PsiCall call = getCall(Set.of(oppModel.XSD_BOOLEAN_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE));
        IIfOperator.INSTANCE.validate(call, holder);
        verify(holder, never())
                .registerProblem(call, "Possible outcomes are incompatible, not illegal but it smells fishy", ProblemHighlightType.WEAK_WARNING);
    }

    @Test
    void testIsStatic() {
        Assertions.assertTrue(IIfOperator.INSTANCE.isStatic());
    }
}
