package com.misset.opp.odt.annotation;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class CodeAnnotatorUnresolvableTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.emptyList();
    }

    @Test
    void testHasCallIsNotDeclaredWarning() {
        configureByText(insideQueryWithPrefixes("query"));
        assertHasError("query is not declared");
        // fixes are tested at OMTImportUtil
    }
}
