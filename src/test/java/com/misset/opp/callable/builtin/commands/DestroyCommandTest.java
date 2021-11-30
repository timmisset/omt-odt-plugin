package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class DestroyCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsVoid(DestroyCommand.INSTANCE);
    }

    @Test
    void testValidateThrowsErrorWhenNotInstances() {
        final PsiCall call = getCall(Set.of(OppModel.INSTANCE.XSD_NUMBER),
                Set.of(OppModel.INSTANCE.XSD_NUMBER_INSTANCE));
        DestroyCommand.INSTANCE.validate(call, holder);
        verify(holder).registerProblem(firstArgument,
                TTLValidationUtil.ERROR_MESSAGE_INSTANCES,
                ProblemHighlightType.ERROR);
    }

    @Test
    void testValidateThrowsNoErrorWhenInstances() {
        final PsiCall call = getCall(Set.of(OppModel.INSTANCE.XSD_NUMBER_INSTANCE),
                Set.of(OppModel.INSTANCE.XSD_NUMBER_INSTANCE));
        DestroyCommand.INSTANCE.validate(call, holder);
        verify(holder, never()).registerProblem(firstArgument,
                TTLValidationUtil.ERROR_MESSAGE_INSTANCES,
                ProblemHighlightType.ERROR);
    }
}
