package com.misset.opp.omt.meta;

import com.misset.opp.testCase.OMTCompletionTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTExportMemberMetaTypeTest extends OMTCompletionTestCase {

    @Test
    void testShowsImportedMembers() {
        myFixture.configureByText("moduleFile.module.omt",
                "import:\n" +
                        "   ./importedFile.omt:\n" +
                        "   - commandA\n" +
                        "   - queryA\n" +
                        "export:\n" +
                        "- <caret>\n" +
                        "- queryA\n" +
                        "");

        List<String> lookupStrings = getLookupStrings();
        assertContainsElements(lookupStrings, "commandA");
        assertDoesntContain(lookupStrings, "queryA");
    }

}