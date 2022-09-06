package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.testCase.ODTTestCase;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

class ODTResolvableSignatureArgumentTest extends ODTTestCase {

    public static Stream<Arguments> getCommandArguments() {
        return Stream.of(
                Arguments.of("{ RETURN 'a'; }", OppModelConstants.getXsdStringInstance()),
                Arguments.of("{ 'a'; }", OppModelConstants.getVoidResponse()),
                Arguments.of("'a'", OppModelConstants.getXsdStringInstance())
        );
    }

    @ParameterizedTest
    @MethodSource("getCommandArguments")
    void testResolveSignatureArgument(String argument, OntResource result) {
        ODTFile odtFile = configureByText(String.format("@COMMAND(%s)", argument));
        ReadAction.run(() -> {
            ODTSignatureArgument signatureArgument = PsiTreeUtil.findChildOfType(odtFile, ODTSignatureArgument.class, true);
            Set<OntResource> resolve = Objects.requireNonNull(signatureArgument).resolve();
            assertContainsElements(resolve, result);
        });
    }

    @Test
    void testIsPrimitiveArgumentTrue() {
        ODTFile odtFile = configureByText("@COMMAND(1)");
        ReadAction.run(() -> {
            ODTResolvableSignatureArgument signatureArgument = PsiTreeUtil.findChildOfType(odtFile, ODTResolvableSignatureArgument.class, true);
            assertTrue(Objects.requireNonNull(signatureArgument).isPrimitiveArgument());
        });
    }

    @Test
    void testIsPrimitiveArgumentFalse() {
        ODTFile odtFile = configureByText("@COMMAND($value)");
        ReadAction.run(() -> {
            ODTResolvableSignatureArgument signatureArgument = PsiTreeUtil.findChildOfType(odtFile, ODTResolvableSignatureArgument.class, true);
            assertFalse(Objects.requireNonNull(signatureArgument).isPrimitiveArgument());
        });
    }

    @Test
    void testTypeFilterDoesntAcceptVoid() {
        ODTFile odtFile = configureByText("@COMMAND({})");
        ReadAction.run(() -> {
            ODTResolvableSignatureArgument signatureArgument = PsiTreeUtil.findChildOfType(odtFile, ODTResolvableSignatureArgument.class, true);
            Predicate<Set<OntResource>> typeFilter = Objects.requireNonNull(signatureArgument).getTypeFilter(signatureArgument);
            assertFalse(typeFilter.test(Collections.singleton(OppModelConstants.getVoidResponse())));
        });
    }

    @Test
    void testTypeFilterDoesntAcceptStrangeType() {
        initOntologyModel();
        ODTFile odtFile = configureByText("CONTAINS(1)");
        ReadAction.run(() -> {
            ODTResolvableSignatureArgument signatureArgument = PsiTreeUtil.findChildOfType(odtFile, ODTResolvableSignatureArgument.class, true);
            Predicate<Set<OntResource>> typeFilter = signatureArgument.getTypeFilter(signatureArgument);
            assertFalse(typeFilter.test(Collections.singleton(OppModelConstants.getXsdIntegerInstance())));
            assertTrue(typeFilter.test(Collections.singleton(OppModelConstants.getXsdStringInstance())));
        });
    }
}
