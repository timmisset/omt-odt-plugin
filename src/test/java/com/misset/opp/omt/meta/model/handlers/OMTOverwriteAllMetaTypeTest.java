package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTOverwriteAllMetaTypeTest extends OMTTestCase {

    @Test
    void testCompletionAddsEmptyBlock() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  <caret>" +
                        "");
        configureByText(content);
        List<String> lookupStrings = completion.getLookupStrings();
        assertTrue(lookupStrings.contains("!OverwriteAll {}"));
    }

}
