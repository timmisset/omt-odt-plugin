package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
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
        assertResolved(FormatOperator.INSTANCE, OntologyModelConstants.getXsdStringInstance());
    }

    @Test
    void testUnequalNumberOfArguments() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()));
        doReturn("Test %s").when(call).getSignatureValue(0);
        FormatOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(eq(call), startsWith("Unequal number"), eq(ProblemHighlightType.ERROR));
    }

    @Test
    void testWrongTypesString() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()), Set.of(OntologyModelConstants.getXsdIntegerInstance()));
        doReturn("Test %s").when(call).getSignatureValue(0);
        FormatOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(secondArgument, OntologyValidationUtil.ERROR_MESSAGE_STRING, ProblemHighlightType.ERROR);
    }

    @Test
    void testWrongTypesNumber() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()), Set.of(OntologyModelConstants.getXsdStringInstance()));
        doReturn("Test %d").when(call).getSignatureValue(0);
        FormatOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(secondArgument, OntologyValidationUtil.ERROR_MESSAGE_NUMBER, ProblemHighlightType.ERROR);
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
        assertGetAcceptableArgumentType(FormatOperator.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentTypeIsNull(FormatOperator.INSTANCE, 1);
    }

}
