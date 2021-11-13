package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.omt.inspection.OMTValueInspection;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ODTQueryMetaTypeTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTValueInspection.class);
    }

    @Test
    void testHasError() {
        String content = insideStandaloneQueryWithPrefixes("query: " +
                "'hello';\n");
        configureByText(content);
        assertHasError("Query value should not contain semicolon ending");
    }
}
