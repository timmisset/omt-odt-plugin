package com.misset.opp.omt.meta;

import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTImportedMemberRefMetaTypeTest extends OMTTestCase {

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

        List<String> lookupStrings = completion.getLookupStrings();
        assertContainsElements(lookupStrings, "commandA");
        assertDoesntContain(lookupStrings, "queryA");
    }

}
