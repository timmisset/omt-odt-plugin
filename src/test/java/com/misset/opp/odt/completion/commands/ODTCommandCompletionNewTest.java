package com.misset.opp.odt.completion.commands;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionNewTest extends OMTCompletionTestCase {

    @Test
    void testShowsPredicates() {
        String content = insideProcedureRunWithPrefixes(
                "@NEW(<caret>)");
        configureByText(content, true);
        oppModel.createClass("http://test1");
        oppModel.createClass("http://test2");
        oppModel.createClass("http://test3");
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "/<http://test1>", "/<http://test2>", "/<http://test3>");
    }

}
