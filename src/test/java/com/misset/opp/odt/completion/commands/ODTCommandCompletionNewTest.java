package com.misset.opp.odt.completion.commands;

import com.misset.opp.testCase.OMTCompletionTestCase;
import com.misset.opp.ttl.OppModel;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTCommandCompletionNewTest extends OMTCompletionTestCase {

    @Test
    void testShowsPredicates() {
        String content = insideProcedureRunWithPrefixes(
                "@NEW(<caret>)");
        configureByText(content, true);
        OppModel.INSTANCE.getModel().createClass("http://test1");
        OppModel.INSTANCE.getModel().createClass("http://test2");
        OppModel.INSTANCE.getModel().createClass("http://test3");
        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "/<http://test1>", "/<http://test2>", "/<http://test3>");
    }

}