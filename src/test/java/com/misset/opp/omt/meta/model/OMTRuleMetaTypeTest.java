package com.misset.opp.omt.meta.model;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTRuleMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTMissingKeysInspection.class);
    }

    @Test
    void testHasErrorWhenMissingQuery() {
        configureByText(insideActivityWithPrefixes(
                "rules:\n" +
                        "   ruleA:\n" +
                        "       strict: true\n"
        ));
        assertHasError("Missing required key(s): 'query'");
    }

    @Test
    void testHasNoErrorWhenQueryIsPresent() {
        configureByText(insideActivityWithPrefixes(
                "rules:\n" +
                        "   ruleA:\n" +
                        "       query: true\n" +
                        "       strict: true\n"
        ));
        assertNoError("Missing required key(s): 'query'");
    }
}