package com.misset.opp.omt.meta.model.handlers;

import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OMTForbiddenPredicatesMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Set.of(OMTMissingKeysInspection.class, OMTUnkownKeysInspection.class, OMTValueInspection.class));
    }

    @Test
    void testMergeListMissingRequiredKeys() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !ForbiddenPredicates\n" +
                        "   context: b\n" +
                        ""
        );
        configureByText(content);
        inspection.assertHasError("Missing required key(s): 'predicates'");
    }

    @Test
    void testMergeListAccepted() {
        String content = insideActivityWithPrefixes(
                "handlers:\n" +
                        "-  !ForbiddenPredicates\n" +
                        "   context: current\n" +
                        "   predicates: /a:b\n" +
                        ""
        );
        configureByText(content);
        inspection.assertNoError("Missing required key(s):");
    }
}
