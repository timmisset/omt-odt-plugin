package com.misset.opp.omt.meta.model;

import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTRuleMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTMissingKeysInspection.class));
    }

    @Test
    void testHasErrorWhenMissingQuery() {
        configureByText(insideActivityWithPrefixes(
                "rules:\n" +
                        "   ruleA:\n" +
                        "       strict: true\n"
        ));
        inspection.assertHasError("Missing required key(s): 'query'");
    }

    @Test
    void testHasNoErrorWhenQueryIsPresent() {
        configureByText(insideActivityWithPrefixes(
                "rules:\n" +
                        "   ruleA:\n" +
                        "       query: true\n" +
                        "       strict: true\n"
        ));
        inspection.assertNoError("Missing required key(s): 'query'");
    }
}
