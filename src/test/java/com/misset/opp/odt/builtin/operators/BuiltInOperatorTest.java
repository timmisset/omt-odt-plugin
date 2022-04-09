package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
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
final class BuiltInOperatorTest extends BaseBuiltinTest {

    @Test
    void testType() {
        Assertions.assertEquals("Builtin Operator", LogOperator.INSTANCE.getType());
    }

    @Test
    void testCanBeAppliedTo() {
        Assertions.assertTrue(LogOperator.INSTANCE.canBeAppliedTo(Set.of(oppModel.XSD_STRING_INSTANCE)));
    }

    @Test
    void testIsCommand() {
        Assertions.assertFalse(LogOperator.INSTANCE.isCommand());
    }

    @Test
    void testIsStatic() {
        Assertions.assertFalse(LogOperator.INSTANCE.isStatic());
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
    protected void testResolve() {
        // not necessary for abstract class
    }

    @Test
    void testValidateCompatibleOutcomePossibilitiesReturnsWithoutValidationWhenResourceEmpty() {
        Set<OntResource> withValues = Set.of(oppModel.XSD_STRING_INSTANCE);
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

}
