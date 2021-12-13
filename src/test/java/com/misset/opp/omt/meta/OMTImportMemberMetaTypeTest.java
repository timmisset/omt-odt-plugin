package com.misset.opp.omt.meta;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.misset.opp.omt.meta.OMTImportMemberMetaType.NOT_AN_EXPORTED_MEMBER;

class OMTImportMemberMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTValueInspection.class);
    }

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

    @Test
    void testValidateScalarValue() {
        configureByText("queries.omt", "queries:\n" +
                "   DEFINE QUERY queryA => '1';\n" +
                "   DEFINE QUERY queryB => '2';\n" +
                "");
        configureByText("import:\n" +
                "   ./queries.omt:\n" +
                "   - queryA\n" +
                "   - queryB\n" +
                "   - queryC\n" +
                "");
        assertNoError(NOT_AN_EXPORTED_MEMBER.apply("queryA"));
        assertNoError(NOT_AN_EXPORTED_MEMBER.apply("queryB"));
        assertHasError(NOT_AN_EXPORTED_MEMBER.apply("queryC"));
    }
}
