package com.misset.opp.omt.meta.model.handlers;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

class OMTForbiddenPredicatesMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return List.of(OMTMissingKeysInspection.class, OMTUnkownKeysInspection.class, OMTValueInspection.class);
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
        assertHasError("Missing required key(s): 'predicates'");
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
        assertNoError("Missing required key(s):");
    }
}
