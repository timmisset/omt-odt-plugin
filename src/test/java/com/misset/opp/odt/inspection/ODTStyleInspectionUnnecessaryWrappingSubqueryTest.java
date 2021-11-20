package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.ODTStyleInspectionUnnecessaryWrappingSubquery.WARNING;

class ODTStyleInspectionUnnecessaryWrappingSubqueryTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTStyleInspectionUnnecessaryWrappingSubquery.class);
    }

    @Test
    void testWrappedSubquery() {
        String content = insideQueryWithPrefixes("('' == '') AND ('' == '')");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testWrappedSubqueryNoWarning() {
        String content = insideQueryWithPrefixes("('' == '' OR '' == '') AND ('' == '' AND '' == '')");
        configureByText(content);
        assertNoWarning(WARNING);
    }

    @Test
    void testWrappedSubqueryInIFBlock() {
        String content = insideProcedureRunWithPrefixes("IF ('' == '') { @LOG('test'); }");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testUnwrapSubquery() {
        String content = insideQueryWithPrefixes("('' == '')");
        configureByText(content);
        Assertions.assertTrue(getFile().getText().contains("('' == '')"));
        invokeQuickFixIntention("Unwrap");
        Assertions.assertTrue(getFile().getText().contains("'' == ''"));
        Assertions.assertFalse(getFile().getText().contains("('' == '')"));
    }
}
