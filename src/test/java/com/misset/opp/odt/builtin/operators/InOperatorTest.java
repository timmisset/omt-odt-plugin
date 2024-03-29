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

class InOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(InOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("IN", InOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, InOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, InOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetFlags() {
        Assertions.assertEquals(AbstractBuiltInOperator.IGNORE_CASE_FLAG, InOperator.INSTANCE.getFlags());
    }

    @Test
    void testSpecificValidationShowsWarningWhenIncompatiblePreviousStepAndArgumentTypes() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(call).resolvePreviousStep();
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        InOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(eq(call), startsWith("Incompatible types"), eq(ProblemHighlightType.WARNING));
    }
}
