package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class FormatOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FormatOperator.INSTANCE, OppModelConstants.getXsdStringInstance());
    }

    @Test
    void testUnequalNumberOfArguments() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdStringInstance()));
        doReturn("Test %s").when(call).getSignatureValue(0);
        FormatOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(eq(call), startsWith("Unequal number"), eq(ProblemHighlightType.ERROR));
    }

    @Test
    void testWrongTypesString() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdStringInstance()), Set.of(OppModelConstants.getXsdIntegerInstance()));
        doReturn("Test %s").when(call).getSignatureValue(0);
        FormatOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(secondArgument, TTLValidationUtil.ERROR_MESSAGE_STRING, ProblemHighlightType.ERROR);
    }

    @Test
    void testWrongTypesNumber() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdStringInstance()), Set.of(OppModelConstants.getXsdStringInstance()));
        doReturn("Test %d").when(call).getSignatureValue(0);
        FormatOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(secondArgument, TTLValidationUtil.ERROR_MESSAGE_NUMBER, ProblemHighlightType.ERROR);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FORMAT", FormatOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, FormatOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, FormatOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(FormatOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(FormatOperator.INSTANCE, 1);
    }

}
