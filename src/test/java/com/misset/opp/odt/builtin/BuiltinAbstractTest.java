package com.misset.opp.odt.builtin;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.operators.LogOperator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class BuiltinAbstractTest extends BaseBuiltinTest {

    @Test
    public void testSpecificValidationDoesNothing() {
        PsiCall call = mock(PsiCall.class);
        LogOperator.INSTANCE.specificValidation(call, holder);

        verify(holder, never()).registerProblem(any(), anyString(), any(ProblemHighlightType.class));
    }

    @Test
    public void testGetAcceptableArgumentTypeWithContextReturnsNull() {
        Assertions.assertNull(LogOperator.INSTANCE.getAcceptableArgumentTypeWithContext(0, null));
    }

    @Override
    @Test
    protected void testResolve() {
        Assertions.assertNull(LogOperator.INSTANCE.resolveSingle());
    }

    @Test
    void testResolveError() {
        Set<OntResource> resources = Set.of(oppModel.XSD_STRING_INSTANCE);
        Assertions.assertEquals(resources, LogOperator.INSTANCE.resolveError(resources, null));
    }

    @Test
    void testGetParameterNamesReturnsNewMap() {
        Assertions.assertTrue(LogOperator.INSTANCE.getParameterNames().isEmpty());
    }
}
