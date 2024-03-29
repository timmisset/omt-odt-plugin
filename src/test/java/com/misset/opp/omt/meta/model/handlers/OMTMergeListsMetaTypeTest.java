package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OMTMergeListsMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Set.of(OMTMissingKeysInspection.class, OMTUnkownKeysInspection.class, OMTValueInspection.class));
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
        inspection.assertHasError("Missing required key(s): 'subjects'");
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
        inspection.assertNoError("Missing required key(s):");
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
        inspection.assertHasError(OMTMergePredicatesMetaType.CANNOT_COMBINE_ANY_PREDICATE_AND_PREDICATES);
    }

    @Test
    void testMergeHandlerEitherPredicateOrAnyPredicateIsRequired() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !MergeLists\n" +
                        "   subjects: /a:b\n" +
                        ""
        );
        configureByText(content);
        inspection.assertHasError(OMTMergePredicatesMetaType.ANY_PREDICATE_OR_PREDICATES_IS_REQUIRED);
    }
}
