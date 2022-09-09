package com.misset.opp.odt.completion.commands;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionNewTest extends ODTTestCase {

    @Test
    void testShowsPredicates() {
        String content = "@NEW(<caret>)";
        configureByText(content, true);
        oppModel.createClass("http://test1");
        oppModel.createClass("http://test2");
        oppModel.createClass("http://test3");
        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "/<http://test1>", "/<http://test2>", "/<http://test3>");
    }

}
