package com.misset.opp.omt.meta.model.handlers;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

class OMTMergeListsMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return List.of(OMTMissingKeysInspection.class, OMTUnkownKeysInspection.class, OMTValueInspection.class);
    }

    @Test
    void testMergeListMissingRequiredKeys() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergeLists\n" +
                        "   when: b\n" +
                        ""
        );
        configureByText(content);
        assertHasError("Missing required key(s): 'predicates, subjects'");
    }

    @Test
    void testMergeListAccepted() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergeLists\n" +
                        "   predicates: /a:b\n" +
                        "   subjects: /a:b\n" +
                        "   when: b\n" +
                        ""
        );
        configureByText(content);
        assertNoError("Missing required key(s):");
    }

    @Test
    void testMergeHandlerUsingBothPredicatesFields() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergeLists\n" +
                        "   predicates: /b\n" +
                        "   anyPredicate: true\n" +
                        ""
        );
        configureByText(content);
        assertHasError(OMTMergePredicatesMetaType.CANNOT_COMBINE_ANY_PREDICATE_AND_PREDICATES);
    }
}
