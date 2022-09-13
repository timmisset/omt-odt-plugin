package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiReference;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class ODTJavaDocReferenceContributorTest extends ODTTestCase {

    @Test
    void testHasParameterPrefixAndClassReference() {
        String content = withPrefixes("/**\n" +
                " * @param $param (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY query => true;");
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            PsiDocTag docTag = PsiTreeUtil.findChildOfType(odtFileTest, PsiDocTag.class);
            PsiReference[] references = docTag.getReferences();
            assertEquals(3, references.length);
            assertTrue(Arrays.stream(references).anyMatch(psiReference -> psiReference instanceof ODTParameterAnnotationReference));
            assertTrue(Arrays.stream(references).anyMatch(psiReference -> psiReference instanceof ODTTypePrefixAnnotationReference));
            assertTrue(Arrays.stream(references).anyMatch(psiReference -> psiReference instanceof ODTJavaDocTTLSubjectReference));
        });
    }

    @Test
    void testHasPrefixAndClassReference() {
        String content = withPrefixes("/**\n" +
                " * @base (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY query => true;");
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            PsiDocTag docTag = PsiTreeUtil.findChildOfType(odtFileTest, PsiDocTag.class);
            PsiReference[] references = docTag.getReferences();
            assertEquals(2, references.length);
            assertTrue(Arrays.stream(references).anyMatch(psiReference -> psiReference instanceof ODTTypePrefixAnnotationReference));
            assertTrue(Arrays.stream(references).anyMatch(psiReference -> psiReference instanceof ODTJavaDocTTLSubjectReference));
        });
    }

    @Test
    void testHasParameterClassReference() {
        String content = withPrefixes("/**\n" +
                " * @param $param (<http://ontology/ClassA>)\n" +
                " */\n" +
                "DEFINE QUERY query => true;");
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            PsiDocTag docTag = PsiTreeUtil.findChildOfType(odtFileTest, PsiDocTag.class);
            PsiReference[] references = docTag.getReferences();
            assertEquals(2, references.length);
            assertTrue(Arrays.stream(references).anyMatch(psiReference -> psiReference instanceof ODTParameterAnnotationReference));
            assertTrue(Arrays.stream(references).anyMatch(psiReference -> psiReference instanceof ODTJavaDocTTLSubjectReference));
        });
    }

    @Test
    void testHasClassReference() {
        String content = withPrefixes("/**\n" +
                " * @base (<http://ontology/ClassA>)\n" +
                " */\n" +
                "DEFINE QUERY query => true;");
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            PsiDocTag docTag = PsiTreeUtil.findChildOfType(odtFileTest, PsiDocTag.class);
            PsiReference[] references = docTag.getReferences();
            assertEquals(1, references.length);
            assertTrue(Arrays.stream(references).anyMatch(psiReference -> psiReference instanceof ODTJavaDocTTLSubjectReference));
        });
    }

}
