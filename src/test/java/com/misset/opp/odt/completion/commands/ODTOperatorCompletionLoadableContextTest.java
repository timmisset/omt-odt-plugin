package com.misset.opp.odt.completion.commands;

import com.misset.opp.omt.meta.model.modelitems.OMTLoadableMetaType;
import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class ODTOperatorCompletionLoadableContextTest extends OMTCompletionTestCase {

    @Test
    void testShowsContextOptions() {
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       id: test\n" +
                "       path: test.json\n" +
                "       schema: test.json\n" +
                "   Procedure: !Procedure\n" +
                "       onRun:\n" +
                "           VAR $data = MyLoadable!retain(<caret>);";
        ;
        configureByText(content, true);
        List<String> lookupStrings = getLookupStrings();
        Assertions.assertEquals(OMTLoadableMetaType.getContextSelectors().size(), lookupStrings.size());
        assertContainsElements(lookupStrings, "'omt'", "'session'");
    }

}
