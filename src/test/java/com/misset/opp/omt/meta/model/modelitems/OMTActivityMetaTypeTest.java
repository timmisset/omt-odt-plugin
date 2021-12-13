package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTActivityMetaTypeTest extends OMTCompletionTestCase {

    @Test
    void computeKeyCompletions() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       title: title\n" +
                "       <caret>\n" +
                "\n";
        configureByText(content);
        final List<String> completionList = getLookupStrings();
        new OMTActivityMetaType().getFeatures()
                .keySet()
                .stream()
                .filter(s -> !s.equals("title"))
                .forEach(s -> Assertions.assertTrue(completionList.contains(s)));
    }
}
