package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
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
        final PsiCall call = getCall(Set.of(OppModelConstants.getXsdNumber()),
                Set.of(OppModelConstants.getXsdNumberInstance()));
        DestroyCommand.INSTANCE.validate(call, holder);
        verify(holder).registerProblem(firstArgument,
                TTLValidationUtil.ERROR_MESSAGE_INSTANCES,
                ProblemHighlightType.ERROR);
    }

    @Test
    void testValidateThrowsNoErrorWhenInstances() {
        final PsiCall call = getCall(Set.of(OppModelConstants.getXsdNumberInstance()),
                Set.of(OppModelConstants.getXsdNumberInstance()));
        DestroyCommand.INSTANCE.validate(call, holder);
        verify(holder, never()).registerProblem(firstArgument,
                TTLValidationUtil.ERROR_MESSAGE_INSTANCES,
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
