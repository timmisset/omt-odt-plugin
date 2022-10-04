package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.meta.arrays.OMTHandlersArrayMetaType;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
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

    @Test
    void testHasWarningWhenCombiningOverwriteAllWithOtherHandlers() {
        myFixture.enableInspections(Collections.singleton(OMTValueInspection.class));
        String content = "model:\n" +
                "   GraphShapeHandler: !GraphShapeHandlers\n" +
                "       id: myId\n" +
                "       handlers:\n" +
                "       -  !OverwriteAll {} \n" +
                "       -  !MergePredicates\n" +
                "          subjects: /a:b\n" +
                "          predicates: /a:b\n" +
                "          from: parent\n" +
                "\n";
        configureByText(content);
        inspection.assertHasWarning(OMTHandlersArrayMetaType.OVERWRITE_ALL_SHOULD_NOT_BE_COMBINED_WITH_OTHER_HANDLERS);
    }

}
