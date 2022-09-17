package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

class EqualsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(EqualsOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testFlagShowsWarningWhenUsedOnNonStringValuesDefault() {
        PsiElement flagElement = mock(PsiElement.class);
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdBooleanInstance()), Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        doReturn(flagElement).when(call).getFlagElement();
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        EqualsOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(flagElement, "Using ignoreCase on non-string values", ProblemHighlightType.WARNING);
    }

    @Test
    void testSpecificValidationShowsWarningWhenIncompatibleArgumentTypes() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()), Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        EqualsOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(eq(call), startsWith("Incompatible types"), eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testSpecificValidationShowsNoWarningWhenTooManyArgumentsToCompare() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()), Set.of(OntologyModelConstants.getXsdBooleanInstance()), Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        EqualsOperator.INSTANCE.specificValidation(call, holder);
        verify(holder, never()).registerProblem(eq(call), startsWith("Incompatible types"), eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testSpecificValidationShowsWarningWhenIncompatiblePreviousStepAndArgumentTypes() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(call).resolvePreviousStep();
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        EqualsOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(eq(call), startsWith("Incompatible types"), eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testName() {
        Assertions.assertEquals("EQUALS", EqualsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, EqualsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, EqualsOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentTypeSameAsPreviousStep(EqualsOperator.INSTANCE, 0);
        assertGetAcceptableArgumentTypeSameAsArgument(EqualsOperator.INSTANCE, 1, 0);
        assertGetAcceptableArgumentTypeIsNull(EqualsOperator.INSTANCE, 2);
    }

    @Test
    void testGetFlags() {
        Assertions.assertEquals(AbstractBuiltInOperator.IGNORE_CASE_FLAG, EqualsOperator.INSTANCE.getFlags());
    }
}
