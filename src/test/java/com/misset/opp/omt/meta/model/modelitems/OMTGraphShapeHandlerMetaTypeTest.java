package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTGraphShapeHandlerMetaTypeTest extends OMTTestCase {

    @Test
    void testComputeKeyCompletions() {

        String content = "model:\n" +
                "   GraphShapeHandler: !GraphShapeHandlers\n" +
                "       id: myId\n" +
                "       <caret>\n" +
                "\n";
        configureByText(content);
        final List<String> completionList = completion.getLookupStrings();
        OMTGraphShapeHandlerMetaType.getInstance().getFeatures()
                .keySet()
                .stream()
                .filter(s -> !s.equals("id"))
                .forEach(s -> Assertions.assertTrue(completionList.contains(s)));
    }


}
