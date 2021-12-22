package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTUnusedPrefixIriInspectionTest extends OMTInspectionTestCase {
    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnusedPrefixIriInspection.class);
    }

    @Test
    void testOMTPrefixUnused() {
        String content = "prefixes:\n" +
                "   ont: <http://ontology/>";
        configureByText(content);
        assertHasWarning("ont is never used");
    }

    @Test
    void testOMTPrefixUsed() {
        String content = "prefixes:\n" +
                "   ont: <http://ontology/>\n" +
                "\n" +
                "queries: |\n" +
                "   DEFINE QUERY query => ont:ClassA;\n" +
                "";
        configureByText(content);
        assertNoWarning("ont is never used");
    }
}