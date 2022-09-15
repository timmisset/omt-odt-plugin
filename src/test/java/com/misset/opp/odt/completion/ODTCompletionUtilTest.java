package com.misset.opp.odt.completion;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCompletionUtilTest extends ODTTestCase {

    @Test
    void testContainsPrefixMatcher() {

        String content = withPrefixes("/ont:<caret>ClassA");
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        assertNotEmpty(lookupStrings);
        assertContainsElements(lookupStrings, "/ont:ClassA", "/ont:ClassB", "/ont:ClassBSub");
    }

    @Test
    void testAppendsExstingPrefixMatcher() {
        String content = withPrefixes("/ont:ClassB<caret>");
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        assertNotEmpty(lookupStrings);
        assertContainsElements(lookupStrings, "/ont:ClassB", "/ont:ClassBSub");
        assertDoesntContain(lookupStrings, "/ont:ClassA");
    }
}
