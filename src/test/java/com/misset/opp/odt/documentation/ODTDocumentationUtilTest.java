package com.misset.opp.odt.documentation;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Test;

import java.util.Set;

class ODTDocumentationUtilTest extends ODTTestCase {

    @Test
    void getTypeFromDocTagWithPrefix() {
        String content = withPrefixes("/**\n" +
                " * @param $param (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY query => true;");
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            PsiDocTag docTag = PsiTreeUtil.findChildOfType(odtFileTest, PsiDocTag.class);

            Set<OntResource> typeFromDocTag = ODTDocumentationUtil.getTypeFromDocTag(docTag, 1);
            assertEquals("http://ontology#ClassA_InstanceA", typeFromDocTag.iterator().next().getURI());
        });
    }

    @Test
    void getTypeFromBaseDocTagWithPrefix() {
        String content = withPrefixes("/**\n" +
                " * @base (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY query => true;");
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            PsiDocTag docTag = PsiTreeUtil.findChildOfType(odtFileTest, PsiDocTag.class);

            Set<OntResource> typeFromDocTag = ODTDocumentationUtil.getTypeFromDocTag(docTag, 0);
            assertEquals("http://ontology#ClassA_InstanceA", typeFromDocTag.iterator().next().getURI());
        });
    }

    @Test
    void getTypeFromDocTagWithQualifiedUri() {
        String content = "/**\n" +
                " * @param $param (<http://ontology#ClassA>)\n" +
                " */\n" +
                "DEFINE QUERY query => true;";
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            PsiDocTag docTag = PsiTreeUtil.findChildOfType(odtFileTest, PsiDocTag.class);

            Set<OntResource> typeFromDocTag = ODTDocumentationUtil.getTypeFromDocTag(docTag, 1);
            assertEquals("http://ontology#ClassA_InstanceA", typeFromDocTag.iterator().next().getURI());
        });
    }
}
