package com.misset.opp.omt.meta.model.handlers;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static com.misset.opp.omt.meta.model.handlers.OMTMergePredicatesMetaType.USE_IS_ONLY_AVAILABLE;
import static com.misset.opp.omt.meta.model.handlers.OMTMergePredicatesMetaType.USE_IS_REQUIRED;

class OMTMergePredicatesTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return List.of(OMTMissingKeysInspection.class, OMTUnkownKeysInspection.class, OMTValueInspection.class);
    }

    @Test
    void testMergeHandlerMissingUseWhenFromIsBoth() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergePredicates\n" +
                        "   subjects: /a:b\n" +
                        "   predicates: /a:b\n" +
                        "   from: both\n" +
                        ""
        );
        configureByText(content);
        assertHasError(USE_IS_REQUIRED);
    }

    @Test
    void testMergeHandlerNotMissingUseWhenFromIsBoth() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergePredicates\n" +
                        "   subjects: /a:b\n" +
                        "   predicates: /a:b\n" +
                        "   from: both\n" +
                        "   use: parent\n" +
                        ""
        );
        configureByText(content);
        assertNoErrors();
    }

    @Test
    void testMergeHandlerUseWhenFromIsNotBoth() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergePredicates\n" +
                        "   subjects: /a\n" +
                        "   predicates: /b\n" +
                        "   from: current\n" +
                        "   use: parent\n" +
                        ""
        );
        configureByText(content);
        assertHasError(USE_IS_ONLY_AVAILABLE);
    }

    @Test
    void testMergeHandlerMissingRequiredFields() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergePredicates\n" +
                        "   from: both\n" +
                        "   use: parent\n" +
                        ""
        );
        configureByText(content);
        assertHasError("Missing required key(s): 'subjects'");
        assertHasError(OMTMergePredicatesMetaType.ANY_PREDICATE_OR_PREDICATES_IS_REQUIRED);
    }

    @Test
    void testMergeHandlerUsingBothPredicatesFields() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergePredicates\n" +
                        "   from: both\n" +
                        "   predicates: /b\n" +
                        "   anyPredicate: true\n" +
                        "   use: parent\n" +
                        ""
        );
        configureByText(content);
        assertHasError("Missing required key(s): 'subjects'");
        assertHasError(OMTMergePredicatesMetaType.CANNOT_COMBINE_ANY_PREDICATE_AND_PREDICATES);
    }
}
