package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.util.Pair;
import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.callable.psi.PsiCall;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class AddToCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertCombinesInput(AddToCommand.INSTANCE);
    }

    @Test
    void testValidateShowsWarningWhenDifferentTypes() {
        final PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_BOOLEAN_INSTANCE));
        AddToCommand.INSTANCE.validate(call, holder);
        verify(holder).registerProblem(eq(signature),
                startsWith("Incompatible types"),
                eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testValidateShowsNoWarningWhenCompatibleTypes() {
        final PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE));
        AddToCommand.INSTANCE.validate(call, holder);
        verify(holder, never()).registerProblem(eq(signature),
                startsWith("Incompatible types"),
                eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testValidateShowsWarningWhenNonMultiple() {
        final PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_STRING_INSTANCE));
        doReturn(new Pair<>(oppModel.getClassIndividuals("http://ontology#ClassA"),
                oppModel.getProperty("http://ontology#classPredicate")))
                .when(call).getSignatureLeadingInformation(0);
        AddToCommand.INSTANCE.validate(call, holder);
        verify(holder).registerProblem(eq(signature),
                startsWith("Suspicious assignment"),
                eq(ProblemHighlightType.WARNING));
    }
}
