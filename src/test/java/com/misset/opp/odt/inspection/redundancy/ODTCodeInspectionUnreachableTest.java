package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.redundancy.ODTCodeInspectionUnreachable.WARNING_MESSAGE;

class ODTCodeInspectionUnreachableTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCodeInspectionUnreachable.class);
    }

    @Test
    void testHasWarningOnUnreachableCodeReturn() {
        configureByText(insideProcedureRunWithPrefixes("RETURN 1; @LOG('test');"));
        assertHasWarning(WARNING_MESSAGE);
    }

    @Test
    void testHasWarningOnUnreachableCodeDone() {
        configureByText(insideProcedureRunWithPrefixes("@DONE(); @LOG('test');"));
        assertHasWarning(WARNING_MESSAGE);
    }

    @Test
    void testHasWarningOnUnreachableCodeCancel() {
        configureByText(insideProcedureRunWithPrefixes("@CANCEL(); @LOG('test');"));
        assertHasWarning(WARNING_MESSAGE);
    }

    @Test
    void testHasNoWarningOnUnreachableCodeReturn() {
        configureByText(insideProcedureRunWithPrefixes("@LOG('test'); RETURN 1;"));
        assertNoWarning(WARNING_MESSAGE);
    }

    @Test
    void testHasNoWarningOnUnreachableCodeDone() {
        configureByText(insideProcedureRunWithPrefixes("@LOG('test'); DONE();"));
        assertNoWarning(WARNING_MESSAGE);
    }

    @Test
    void testHasNoWarningOnUnreachableCodeCancel() {
        configureByText(insideProcedureRunWithPrefixes("@LOG('test'); CANCEL();"));
        assertNoWarning(WARNING_MESSAGE);
    }
}
