package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class DistinctOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(DistinctOperator.INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE, oppModel.XSD_BOOLEAN_INSTANCE);
        assertResolved(DistinctOperator.INSTANCE, oppModel.XSD_DATE, oppModel.XSD_DATE);
        assertResolved(DistinctOperator.INSTANCE, oppModel.XSD_INTEGER, oppModel.XSD_INTEGER);
    }

    @Test
    void testFlagShowsWarningWhenDefault() {
        PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
        doReturn("true").when(call).getSignatureValue(1);
        doReturn("!ignoreCase").when(call).getFlag();

        DistinctOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(secondArgument, "Using both a flag and parameter", ProblemHighlightType.ERROR);
        verify(holder).registerProblem(secondArgument, "Use !ignoreCase flag instead", ProblemHighlightType.WEAK_WARNING);
    }

    @Test
    void testName() {
        Assertions.assertEquals("DISTINCT", DistinctOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, DistinctOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, DistinctOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetFlags() {
        Assertions.assertEquals(BuiltInOperator.IGNORE_CASE_FLAG, DistinctOperator.INSTANCE.getFlags());
    }
}
