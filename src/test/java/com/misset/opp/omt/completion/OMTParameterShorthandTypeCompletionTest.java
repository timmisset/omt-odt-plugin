package com.misset.opp.omt.completion;

import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTParameterShorthandTypeCompletionTest extends OMTTestCase {

    @Test
    void testShowsCompletions() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $param (<caret>)");
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "ont:ClassA", "ont:ClassB", "xsd:integer");
    }

    @Test
    void testShowsCompletionsWithPrefixMatcher() {
        String content = insideActivityWithPrefixes("params:\n" +
                "- $param (ont:<caret>)");
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "ont:ClassA", "ont:ClassB");
        assertDoesntContain(lookupStrings, "xsd:integer");
    }

}
