package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

class HasOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(HasOperator.INSTANCE, OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Test
    void testFlagShowsWarningWhenUsedOnNonStringValuesDefault() {
        PsiElement flagElement = mock(PsiElement.class);
        PsiCall call = getCall(Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE), Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE));
        doReturn(flagElement).when(call).getFlagElement();
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        HasOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(flagElement, "Using ignoreCase on non-string values", ProblemHighlightType.WARNING);
    }

    @Test
    void testSpecificValidationShowsWarningWhenIncompatibleArgumentTypes() {
        PsiCall call = getCall(Set.of(OppModelConstants.XSD_STRING_INSTANCE), Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE));
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        HasOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(eq(call), startsWith("Incompatible types"), eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testSpecificValidationShowsNoWarningWhenTooManyArgumentsToCompare() {
        PsiCall call = getCall(Set.of(OppModelConstants.XSD_STRING_INSTANCE), Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE), Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE));
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        HasOperator.INSTANCE.specificValidation(call, holder);
        verify(holder, never()).registerProblem(eq(call), startsWith("Incompatible types"), eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testSpecificValidationShowsWarningWhenIncompatiblePreviousStepAndArgumentTypes() {
        PsiCall call = getCall(Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE));
        doReturn(Set.of(OppModelConstants.XSD_STRING_INSTANCE)).when(call).resolvePreviousStep();
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        HasOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(eq(call), startsWith("Incompatible types"), eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testName() {
        Assertions.assertEquals("HAS", HasOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, HasOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, HasOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentTypeSameAsPreviousStep(HasOperator.INSTANCE, 0);
        assertGetAcceptableArgumentTypeSameAsArgument(HasOperator.INSTANCE, 1, 0);
        assertGetAcceptableArgumentTypeIsNull(HasOperator.INSTANCE, 2);
    }

    @Test
    void testGetFlags() {
        Assertions.assertEquals(AbstractBuiltInOperator.IGNORE_CASE_FLAG, HasOperator.INSTANCE.getFlags());
    }
}
