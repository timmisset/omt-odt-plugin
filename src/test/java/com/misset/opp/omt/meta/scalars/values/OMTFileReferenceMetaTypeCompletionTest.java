package com.misset.opp.omt.meta.scalars.values;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTFileReferenceMetaTypeCompletionTest extends OMTCompletionTestCase {

    @Test
    void testShowsJsonFilesAsSuggestions() {
        myFixture.addFileToProject("test.json", "");
        String content = "model:\n" +
                "   MyLoadable: !Loadable\n" +
                "       path: <caret>";
        configureByText(content);
        List<String> lookupStrings = getLookupStrings();
        Assertions.assertEquals(1, lookupStrings.size());
        Assertions.assertEquals("./test.json", lookupStrings.get(0));
    }

}
