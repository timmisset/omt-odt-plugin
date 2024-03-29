package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.testcase.ODTTestCase;
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
                Arguments.of("{ RETURN 'a'; }", OntologyModelConstants.getXsdStringInstance()),
                Arguments.of("{ 'a'; }", OntologyModelConstants.getVoidResponse()),
                Arguments.of("'a'", OntologyModelConstants.getXsdStringInstance())
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
            ODTSignatureArgument signatureArgument = PsiTreeUtil.findChildOfType(odtFile, ODTSignatureArgument.class, true);
            assertTrue(Objects.requireNonNull(signatureArgument).isPrimitiveArgument());
        });
    }

    @Test
    void testIsPrimitiveArgumentFalse() {
        ODTFile odtFile = configureByText("@COMMAND($value)");
        ReadAction.run(() -> {
            ODTSignatureArgument signatureArgument = PsiTreeUtil.findChildOfType(odtFile, ODTSignatureArgument.class, true);
            assertFalse(Objects.requireNonNull(signatureArgument).isPrimitiveArgument());
        });
    }

    @Test
    void testTypeFilterDoesntAcceptVoid() {
        ODTFile odtFile = configureByText("@COMMAND({})");
        ReadAction.run(() -> {
            ODTSignatureArgument signatureArgument = PsiTreeUtil.findChildOfType(odtFile, ODTSignatureArgument.class, true);
            Predicate<Set<OntResource>> typeFilter = Objects.requireNonNull(signatureArgument).getTypeFilter(signatureArgument);
            assertFalse(typeFilter.test(Collections.singleton(OntologyModelConstants.getVoidResponse())));
        });
    }

    @Test
    void testTypeFilterDoesntAcceptStrangeType() {
        initOntologyModel(getProject());
        ODTFile odtFile = configureByText("CONTAINS(1)");
        ReadAction.run(() -> {
            ODTSignatureArgument signatureArgument = PsiTreeUtil.findChildOfType(odtFile, ODTSignatureArgument.class, true);
            Predicate<Set<OntResource>> typeFilter = signatureArgument.getTypeFilter(signatureArgument);
            assertFalse(typeFilter.test(Collections.singleton(OntologyModelConstants.getXsdIntegerInstance())));
            assertTrue(typeFilter.test(Collections.singleton(OntologyModelConstants.getXsdStringInstance())));
        });
    }
}
