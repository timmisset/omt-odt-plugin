package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class JoinOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(JoinOperator.INSTANCE, oppModel.XSD_STRING_INSTANCE);
    }

    @Test
    void testName() {
        Assertions.assertEquals("JOIN", JoinOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, JoinOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, JoinOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testSpecificValidationHasErrorWhenNoStringOnIndex0() {
        PsiCall call = getCall(Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
        JoinOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(call.getCallSignatureArgumentElement(0),
                "String required",
                ProblemHighlightType.ERROR);
    }

    @Test
    void testSpecificValidationHasErrorWhenNoStringAsInput() {
        PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE));
        doReturn(Set.of(oppModel.XSD_BOOLEAN_INSTANCE)).when(call).resolvePreviousStep();
        JoinOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(call,
                "String required",
                ProblemHighlightType.ERROR);
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(JoinOperator.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(JoinOperator.INSTANCE, 1);
    }

}
