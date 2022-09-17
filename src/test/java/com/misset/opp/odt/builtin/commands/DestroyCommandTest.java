package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class DestroyCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsVoid(DestroyCommand.INSTANCE);
    }

    @Test
    void testValidateThrowsErrorWhenNotInstances() {
        final PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdNumber()),
                Set.of(OntologyModelConstants.getXsdNumberInstance()));
        DestroyCommand.INSTANCE.validate(call, holder);
        verify(holder).registerProblem(firstArgument,
                OntologyValidationUtil.ERROR_MESSAGE_INSTANCES,
                ProblemHighlightType.ERROR);
    }

    @Test
    void testValidateThrowsNoErrorWhenInstances() {
        final PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdNumberInstance()),
                Set.of(OntologyModelConstants.getXsdNumberInstance()));
        DestroyCommand.INSTANCE.validate(call, holder);
        verify(holder, never()).registerProblem(firstArgument,
                OntologyValidationUtil.ERROR_MESSAGE_INSTANCES,
                ProblemHighlightType.ERROR);
    }

    @Test
    void testName() {
        Assertions.assertEquals("DESTROY", DestroyCommand.INSTANCE.getName());
    }

    @Test
    void testIsVoid() {
        Assertions.assertTrue(ForEachCommand.INSTANCE.isVoid());
    }
}
