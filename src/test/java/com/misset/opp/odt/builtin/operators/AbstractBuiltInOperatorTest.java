package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.*;

/**
 * Do not extend for other tests, always extend from BuiltInTest to prevent test duplications
 */
final class AbstractBuiltInOperatorTest extends BaseBuiltinTest {

    @Test
    void testType() {
        Assertions.assertEquals(CallableType.BUILTIN_OPERATOR, LogOperator.INSTANCE.getType());
    }

    @Test
    void testCanBeAppliedTo() {
        Assertions.assertTrue(LogOperator.INSTANCE.canBeAppliedTo(Set.of(OntologyModelConstants.getXsdStringInstance())));
    }

    @Test
    void testIsCommand() {
        Assertions.assertFalse(LogOperator.INSTANCE.isCommand());
    }

    @Test
    void testRequiresInput() {
        Assertions.assertTrue(LogOperator.INSTANCE.requiresInput());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(LogOperator.INSTANCE.isVoid());
    }

    @Test
    void testGetCallId() {
        Assertions.assertEquals("LOG", LogOperator.INSTANCE.getCallId());
    }

    @Override
    @Test
    protected void testResolve() {
        Assertions.assertNull(LogOperator.INSTANCE.resolveSingle());
    }

    @Test
    void testValidateCompatibleOutcomePossibilitiesReturnsWithoutValidationWhenResourceEmpty() {
        Set<OntResource> withValues = Set.of(OntologyModelConstants.getXsdStringInstance());
        Set<OntResource> emptySet = Collections.emptySet();

        PsiCall call = mock(PsiCall.class);

        // left empty
        LogOperator.INSTANCE.validateCompatibleOutcomePossibilities(
                new Pair<>(emptySet, withValues), call, holder
        );
        // right empty
        LogOperator.INSTANCE.validateCompatibleOutcomePossibilities(
                new Pair<>(withValues, emptySet), call, holder
        );
        verify(holder, never()).registerProblem(any(PsiElement.class), anyString(), any(ProblemHighlightType.class));
    }

    @Test
    void testResolveError() {
        Set<OntResource> resources = Set.of(OntologyModelConstants.getXsdStringInstance());
        Assertions.assertEquals(resources, LogOperator.INSTANCE.resolveError(resources, null));
    }

    @Test
    void testSpecificValidationDoesNothing() {
        PsiCall call = mock(PsiCall.class);
        LogOperator.INSTANCE.specificValidation(call, holder);

        verify(holder, never()).registerProblem(any(), anyString(), any(ProblemHighlightType.class));
    }

}
