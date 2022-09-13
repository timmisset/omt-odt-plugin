package com.misset.opp.odt.inspection;

import com.misset.opp.indexing.PrefixIndex;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.ODTQualifiedURIInspection.USING_FULLY_QUALIFIED_URI;

class ODTQualifiedURIInspectionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTQualifiedURIInspection.class));
        PrefixIndex.addToIndex("ont", "http://ontology#");
    }

    @Test
    void testHasWarning() {
        String content = "DEFINE QUERY query => <http://ontology#ClassA>";
        configureByText(content);
        inspection.assertHasWarning(USING_FULLY_QUALIFIED_URI);
    }

    @Test
    void testReplacesUriWithCurie() {
        String content = "DEFINE QUERY query => <http://ontology#ClassA>";
        configureByText(content);
        inspection.assertHasWarning(USING_FULLY_QUALIFIED_URI);
        inspection.invokeQuickFixIntention(inspection.getAllQuickFixes().get(0));
        Assertions.assertEquals("PREFIX ont: <http://ontology#>;\n" +
                "DEFINE QUERY query => ont:ClassA", getFile().getText());
    }

    @Test
    void testRewriteUriToCurie() {
        String content = withPrefixes("DEFINE QUERY query => <http://ontology#ClassA>");
        configureByText(content);
        inspection.assertHasWarning(USING_FULLY_QUALIFIED_URI);
        inspection.invokeQuickFixIntention(inspection.getAllQuickFixes().get(0));
        Assertions.assertEquals(withPrefixes("DEFINE QUERY query => ont:ClassA"), getFile().getText());
    }

    @Test
    void testReplacesUriWithCurieInAnnotation() {
        String content = "/**\n" +
                " * @param $param (<http://ontology#ClassA>)\n" +
                " */\n" +
                "DEFINE QUERY query($param) => true;\n";
        configureByText(content);
        inspection.assertHasWarning(USING_FULLY_QUALIFIED_URI);
        inspection.invokeQuickFixIntention(inspection.getAllQuickFixes().get(0));
        Assertions.assertEquals(
                "PREFIX ont: <http://ontology#>;\n" +
                        "/**\n" +
                        " * @param $param (ont:ClassA)\n" +
                        " */\n" +
                        "DEFINE QUERY query($param) => true;\n", getFile().getText());
    }
}
