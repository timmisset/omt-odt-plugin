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
        assertResolved(FormatOperator.INSTANCE, OppModelConstants.XSD_STRING_INSTANCE);
    }

    @Test
    void testUnequalNumberOfArguments() {
        PsiCall call = getCall(Set.of(OppModelConstants.XSD_STRING_INSTANCE));
        doReturn("Test %s").when(call).getSignatureValue(0);
        FormatOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(eq(call), startsWith("Unequal number"), eq(ProblemHighlightType.ERROR));
    }

    @Test
    void testWrongTypesString() {
        PsiCall call = getCall(Set.of(OppModelConstants.XSD_STRING_INSTANCE), Set.of(OppModelConstants.XSD_INTEGER_INSTANCE));
        doReturn("Test %s").when(call).getSignatureValue(0);
        FormatOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(eq(secondArgument), eq(TTLValidationUtil.ERROR_MESSAGE_STRING), eq(ProblemHighlightType.ERROR));
    }

    @Test
    void testWrongTypesNumber() {
        PsiCall call = getCall(Set.of(OppModelConstants.XSD_STRING_INSTANCE), Set.of(OppModelConstants.XSD_STRING_INSTANCE));
        doReturn("Test %d").when(call).getSignatureValue(0);
        FormatOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(eq(secondArgument), eq(TTLValidationUtil.ERROR_MESSAGE_NUMBER), eq(ProblemHighlightType.ERROR));
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
        assertGetAcceptableArgumentType(FormatOperator.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(FormatOperator.INSTANCE, 1);
    }

}
