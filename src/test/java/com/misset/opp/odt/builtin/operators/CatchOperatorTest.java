package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

class CatchOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        // no known error state, return input + catch
        assertResolved(CatchOperator.INSTANCE,
                Set.of(oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE,
                        oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testResolveWithError() {
        // known error, return
        assertResolved(CatchOperator.INSTANCE,
                Set.of(oppModel.ERROR),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
    }

    @Test
    void testArgumentTypesCompatibleTypes() {
        PsiCall call = getCall(Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE));
        doReturn(Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE)).when(call).resolvePreviousStep();
        CatchOperator.INSTANCE.specificValidation(call, holder);
        verify(holder, never()).registerProblem(any(), any(), any(ProblemHighlightType.class));
    }

    @Test
    void testArgumentTypesInCompatibleTypes() {
        PsiCall call = getCall(Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE));
        doReturn(Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE)).when(call).resolvePreviousStep();
        CatchOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(eq(call), startsWith("Possible outcomes are incompatible"), any(ProblemHighlightType.class));
    }
}
