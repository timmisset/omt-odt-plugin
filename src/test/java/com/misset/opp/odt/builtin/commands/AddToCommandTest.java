package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.openapi.util.Pair;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.*;

class AddToCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertCombinesInput(AddToCommand.INSTANCE);
    }

    @Test
    void testValidateShowsWarningWhenDifferentTypes() {
        final PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()), Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        AddToCommand.INSTANCE.validate(call, holder);
        verify(holder).registerProblem(eq(signature),
                startsWith("Incompatible types"),
                eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testValidateShowsNoWarningWhenCompatibleTypes() {
        final PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()), Set.of(OntologyModelConstants.getXsdStringInstance()));
        AddToCommand.INSTANCE.validate(call, holder);
        verify(holder, never()).registerProblem(eq(signature),
                startsWith("Incompatible types"),
                eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testValidateShowsWarningWhenNonMultiple() {
        final PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()), Set.of(OntologyModelConstants.getXsdStringInstance()));
        doReturn(new Pair<>(ontologyModel.toIndividuals("http://ontology#ClassA"),
                ontologyModel.getProperty("http://ontology#classPredicate")))
                .when(call).getSignatureLeadingInformation(0);
        AddToCommand.INSTANCE.validate(call, holder);
        verify(holder).registerProblem(eq(signature),
                startsWith("Suspicious assignment"),
                eq(ProblemHighlightType.WARNING));
    }

    @Test
    void testAcceptableArgumentType() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdStringInstance()));
        Assertions.assertTrue(AddToCommand.INSTANCE.getAcceptableArgumentType(1, call)
                .contains(OntologyModelConstants.getXsdStringInstance()));
        Assertions.assertFalse(AddToCommand.INSTANCE.getAcceptableArgumentType(1, call)
                .contains(OntologyModelConstants.getXsdBooleanInstance()));
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsNullForIndexOtherThan1() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> resources = AddToCommand.INSTANCE.getAcceptableArgumentTypeWithContext(0, call);
        Assertions.assertNull(resources);
    }

    @Test
    void testName() {
        Assertions.assertEquals("ADD_TO", AddToCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, AddToCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, AddToCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testAbstractMethods() {
        Assertions.assertEquals("@ADD_TO", AddToCommand.INSTANCE.getCallId());
        Assertions.assertTrue(AddToCommand.INSTANCE.isCommand());
        Assertions.assertEquals(CallableType.BUILTIN_COMMAND, AddToCommand.INSTANCE.getType());
        Assertions.assertFalse(AddToCommand.INSTANCE.canBeAppliedTo(null));
    }
}
