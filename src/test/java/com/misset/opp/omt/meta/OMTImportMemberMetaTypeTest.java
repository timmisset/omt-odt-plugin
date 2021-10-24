package com.misset.opp.omt.meta;

import com.intellij.codeInsight.lookup.LookupElement;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class OMTImportMemberMetaTypeTest extends OMTTestCase {

    @Test
    void testCompletionShowsImportableMembers() {
        configureByText("queries.omt", "queries:\n" +
                "   DEFINE QUERY queryA => '1';\n" +
                "   DEFINE QUERY queryB => '2';\n" +
                "");
        configureByText("import:\n" +
                "   ./queries.omt:\n" +
                "   - <caret>\n" +
                "");
        final LookupElement[] lookupElements = myFixture.completeBasic();
        final List<String> importedMembers = Arrays.stream(lookupElements)
                .map(LookupElement::getLookupString)
                .collect(Collectors.toList());
        Assertions.assertTrue(importedMembers.contains("queryA"));
        Assertions.assertTrue(importedMembers.contains("queryB"));
    }

    @Test
    void testCompletionShowsImportableMembersFiltersExisting() {
        configureByText("queries.omt", "queries:\n" +
                "   DEFINE QUERY queryA => '1';\n" +
                "   DEFINE QUERY queryB => '2';\n" +
                "");
        configureByText("import:\n" +
                "   ./queries.omt:\n" +
                "   - queryA\n" +
                "   - <caret>\n" +
                "");
        final LookupElement[] lookupElements = myFixture.completeBasic();
        final List<String> importedMembers = Arrays.stream(lookupElements)
                .map(LookupElement::getLookupString)
                .collect(Collectors.toList());
        Assertions.assertFalse(importedMembers.contains("queryA"));
        Assertions.assertTrue(importedMembers.contains("queryB"));
    }

}
