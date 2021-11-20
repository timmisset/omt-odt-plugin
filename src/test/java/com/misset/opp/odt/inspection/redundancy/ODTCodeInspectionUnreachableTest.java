package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.redundancy.ODTCodeInspectionUnreachable.WARNING_MESSAGE;

class ODTCodeInspectionUnreachableTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCodeInspectionUnreachable.class);
    }

    @Test
    void testHasWarningOnUnreachableCode() {
        configureByText(insideProcedureRunWithPrefixes("RETURN 1; @LOG('test');"));
        assertHasWarning(WARNING_MESSAGE);
    }

    @Test
    void testHasNoWarningOnUnreachableCode() {
        configureByText(insideProcedureRunWithPrefixes("@LOG('test'); RETURN 1;"));
        assertNoWarning(WARNING_MESSAGE);
    }
}
