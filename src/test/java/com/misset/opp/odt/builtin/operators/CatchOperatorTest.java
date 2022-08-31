package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

class CatchOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        // no known error state, return input + catch
        assertResolved(CatchOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdStringInstance()),
                Set.of(OppModelConstants.getXsdStringInstance(),
                        OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testResolveWithError() {
        // known error, return
        assertResolved(CatchOperator.INSTANCE,
                Set.of(OppModelConstants.getError()),
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testArgumentTypesCompatibleTypes() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdStringInstance()));
        doReturn(Set.of(OppModelConstants.getXsdStringInstance())).when(call).resolvePreviousStep();
        CatchOperator.INSTANCE.specificValidation(call, holder);
        verify(holder, never()).registerProblem(any(), any(), any(ProblemHighlightType.class));
    }

    @Test
    void testArgumentTypesInCompatibleTypes() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdStringInstance()));
        doReturn(Set.of(OppModelConstants.getXsdBooleanInstance())).when(call).resolvePreviousStep();
        CatchOperator.INSTANCE.specificValidation(call, holder);
        verify(holder).registerProblem(eq(call), startsWith("Possible outcomes are incompatible"), any(ProblemHighlightType.class));
    }

    @Test
    void testName() {
        Assertions.assertEquals("CATCH", CatchOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, CatchOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, CatchOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentTypeSameAsPreviousStep(CatchOperator.INSTANCE, 0);
    }
}
