package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class MergeOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        // combine input + argument
        assertResolved(MergeOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdStringInstance()),
                Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdBooleanInstance()),
                Set.of(OntologyModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testResolveTwoOrMoreArguments() {
        // combine arguments
        assertResolved(MergeOperator.INSTANCE,
                Set.of(OntologyModelConstants.getXsdStringInstance()),
                Set.of(OntologyModelConstants.getXsdBooleanInstance(), OntologyModelConstants.getXsdDateInstance()),
                Set.of(OntologyModelConstants.getXsdBooleanInstance()),
                Set.of(OntologyModelConstants.getXsdDateInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("MERGE", MergeOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, MergeOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, MergeOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testSpecificValidationsHasWarningWhenNotAllArgumentsAreEqual() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()), Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        MergeOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(eq(call.getCallSignatureElement()), startsWith("Incompatible types:"), eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testSpecificValidationsHasWarningWhenInputAndArgumentAreNotEqual() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(call).resolvePreviousStep();
        MergeOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(eq(call), startsWith("Incompatible types:"), eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testSpecificValidationHasWarningWhen2OrMoreArgumentsAreCombinedWithAnInput() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()), Set.of(OntologyModelConstants.getXsdStringInstance()));
        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(call).resolvePreviousStep();
        MergeOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(eq(call), startsWith("Using 2 or more arguments will ignore the input value"), eq(ProblemHighlightType.WARNING));
    }
}
