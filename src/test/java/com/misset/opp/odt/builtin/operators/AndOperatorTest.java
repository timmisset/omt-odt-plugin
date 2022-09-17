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

class AndOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(AndOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testName() {
        Assertions.assertEquals("AND", AndOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, AndOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, AndOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(AndOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(AndOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance());
    }

    @Test
    void testArgumentTypes() {
        testArgument(AndOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(AndOperator.INSTANCE, 1, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(AndOperator.INSTANCE, 2, OntologyModelConstants.getXsdBooleanInstance(), OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testValidateSingleArgumentInputBoolean() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(call).resolvePreviousStep();
        ProblemsHolder problemsHolder = mock(ProblemsHolder.class);

        AndOperator.INSTANCE.validateSingleArgumentInputBoolean(call, problemsHolder);
        verify(problemsHolder).registerProblem(call, BuiltInBooleanOperator.ERROR, ProblemHighlightType.ERROR);
    }
}
