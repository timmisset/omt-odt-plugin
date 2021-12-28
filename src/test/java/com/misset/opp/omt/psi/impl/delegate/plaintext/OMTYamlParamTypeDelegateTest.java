package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.psi.references.OMTParamTypePrefixReference;
import com.misset.opp.omt.psi.references.OMTTTLSubjectReference;
import com.misset.opp.testCase.OMTOntologyTestCase;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class OMTYamlParamTypeDelegateTest extends OMTOntologyTestCase {

    @Test
    void testUriHasReferenceToModel() {
        configureByText(insideActivityWithPrefixes(
                "params:\n" +
                        "-  name: $param\n" +
                        "   type: <caret><http://ontology#ClassA>\n"
        ));
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            PsiReference reference = elementAtCaret.getReference();

            Assertions.assertTrue(reference instanceof OMTTTLSubjectReference);
            // StubIndex is not loaded for unit-tests, cannot resolve
        });
    }

    @Test
    void testCurieHasReferenceToModelAndPrefix() {
        configureByText(insideActivityWithPrefixes(
                "params:\n" +
                        "-  name: $param\n" +
                        "   type: ont:Cl<caret>assA\n"
        ));
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            PsiReference @NotNull [] references = elementAtCaret.getReferences();
            Assertions.assertEquals(2, references.length);
            Assertions.assertTrue(Arrays.stream(references).anyMatch(reference -> reference instanceof OMTTTLSubjectReference));
            Assertions.assertTrue(Arrays.stream(references).anyMatch(reference -> reference instanceof OMTParamTypePrefixReference));
            // StubIndex is not loaded for unit-tests, cannot resolve
        });
    }

}
