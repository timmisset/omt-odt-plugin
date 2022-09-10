package com.misset.opp.odt.inspection;

import com.misset.opp.indexing.PrefixIndex;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.ODTQualifiedURIInspection.WEAK_WARNING;

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
        inspection.assertHasWarning(WEAK_WARNING);
    }

    @Test
    void testReplacesUriWithCurie() {
        String content = "DEFINE QUERY query => <http://ontology#ClassA>";
        configureByText(content);
        inspection.assertHasWarning(WEAK_WARNING);
        inspection.invokeQuickFixIntention(inspection.getAllQuickFixes().get(0));
        Assertions.assertEquals("PREFIX ont: <http://ontology#>;\n" +
                "DEFINE QUERY query => ont:ClassA", getFile().getText());
    }
}
