package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTGraphShapeHandlerMetaTypeTest extends OMTCompletionTestCase {

    @Test
    void testComputeKeyCompletions() {

        String content = "model:\n" +
                "   GraphShapeHandler: !GraphShapeHandlers\n" +
                "       id: myId\n" +
                "       <caret>\n" +
                "\n";
        configureByText(content);
        final List<String> completionList = getLookupStrings();
        new OMTGraphShapeHandlerMetaType().getFeatures()
                .keySet()
                .stream()
                .filter(s -> !s.equals("id"))
                .forEach(s -> Assertions.assertTrue(completionList.contains(s)));
    }


}
