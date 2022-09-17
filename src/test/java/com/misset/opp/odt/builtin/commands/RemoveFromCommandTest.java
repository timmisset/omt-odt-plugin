package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Set;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class RemoveFromCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(RemoveFromCommand.INSTANCE);
    }

    @Test
    void testGetName() {
        Assertions.assertEquals("REMOVE_FROM", RemoveFromCommand.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, RemoveFromCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(2, RemoveFromCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testSpecificValidationHasWarningWhenNonMultiple() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getOwlThingInstance()));
        PsiElement element = call.getCallSignatureElement();

        Pair<Set<OntResource>, Property> pair = mock(Pair.class);
        Set<OntResource> subject = mock(Set.class);
        Property predicate = mock(Property.class);
        doReturn(subject).when(pair).getFirst();
        doReturn(predicate).when(pair).getSecond();

        doReturn(pair).when(call).getSignatureLeadingInformation(0);
        ProblemsHolder problemsHolder = mock(ProblemsHolder.class);

        try (MockedStatic<OntologyValidationUtil> mockedStatic = mockStatic(OntologyValidationUtil.class)) {
            RemoveFromCommand.INSTANCE.specificValidation(call, problemsHolder);
            mockedStatic.verify(times(1),
                    () -> OntologyValidationUtil.validateCardinalityMultiple(eq(subject), eq(predicate), eq(problemsHolder), eq(element)));
        }
    }
}
