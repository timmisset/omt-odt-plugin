package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.testCase.CompletionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTActivityMetaTypeTest extends CompletionTestCase {

    @Test
    void computeKeyCompletions() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       title: title\n" +
                "       <caret>\n" +
                "\n";
        final List<String> completionList = getCompletionList(content);
        new OMTActivityMetaType().getFeatures()
                .keySet()
                .stream()
                .filter(s -> !s.equals("title"))
                .forEach(s -> Assertions.assertTrue(completionList.contains(s)));
    }
}
