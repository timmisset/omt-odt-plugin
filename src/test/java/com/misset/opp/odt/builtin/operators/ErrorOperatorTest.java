package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

class ErrorOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(ErrorOperator.INSTANCE, OntologyModelConstants.getError());
    }

    @Test
    void testName() {
        Assertions.assertEquals("ERROR", ErrorOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, ErrorOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, ErrorOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(ErrorOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(ErrorOperator.INSTANCE, 1);
    }

    @Test
    void testSpecificValidationNoErrorWhenValid() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()));

        ErrorOperator.INSTANCE.specificValidation(call, holder);
        verify(holder, never()).registerProblem(any(PsiElement.class), anyString(), eq(ProblemHighlightType.ERROR));
    }

    @Test
    void testSpecificValidationErrorWhenNotValid() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdBooleanInstance()));

        ErrorOperator.INSTANCE.specificValidation(call, holder);
        verify(holder, times(1)).registerProblem(any(PsiElement.class), anyString(), eq(ProblemHighlightType.ERROR));
    }
}
