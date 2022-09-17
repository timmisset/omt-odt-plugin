package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

class IfEmptyOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        // no known error state, return input + catch
        assertResolved(IfEmptyOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdStringInstance()),
                Set.of(OntologyModelConstants.getXsdStringInstance(),
                        OntologyModelConstants.getXsdBooleanInstance()),
                Set.of(OntologyModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("IF_EMPTY", IfEmptyOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, IfEmptyOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, IfEmptyOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentTypeSameAsPreviousStep(IfEmptyOperator.INSTANCE, 0);
    }

    @Test
    void testSpecificValidationThrowsWarningOnDifferentTypes() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(call).resolvePreviousStep();

        IfEmptyOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(call, "Possible outcomes are incompatible, not illegal but it smells fishy", ProblemHighlightType.WEAK_WARNING);
    }

    @Test
    void testSpecificValidationThrowsNoWarningOnIdenticalTypes() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()));
        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(call).resolvePreviousStep();

        IfEmptyOperator.INSTANCE.specificValidation(call, holder);
        verify(holder, never()).registerProblem(call, "Possible outcomes are incompatible, not illegal but it smells fishy", ProblemHighlightType.WEAK_WARNING);
    }

}
