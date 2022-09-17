package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

class OrOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(OrOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("OR", OrOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, OrOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, OrOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(OrOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(OrOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testArgumentTypes() {
        testArgument(OrOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(OrOperator.INSTANCE, 1, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(OrOperator.INSTANCE, 2, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testValidateSingleArgumentInputBoolean() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(call).resolvePreviousStep();
        ProblemsHolder problemsHolder = mock(ProblemsHolder.class);

        OrOperator.INSTANCE.validateSingleArgumentInputBoolean(call, problemsHolder);
        verify(problemsHolder).registerProblem(call, BuiltInBooleanOperator.ERROR, ProblemHighlightType.ERROR);
    }
}
