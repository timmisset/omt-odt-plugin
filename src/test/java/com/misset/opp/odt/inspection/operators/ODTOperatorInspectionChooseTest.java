package com.misset.opp.odt.inspection.operators;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.operators.ODTOperatorInspectionChoose.INCOMPLETE_CHOOSE_EXPECTED_END;
import static com.misset.opp.odt.inspection.operators.ODTOperatorInspectionChoose.INCOMPLETE_CHOOSE_EXPECTED_WHEN_CONDITIONS;
import static com.misset.opp.odt.inspection.operators.ODTOperatorInspectionChoose.OMIT_OTHERWISE;
import static com.misset.opp.odt.inspection.operators.ODTOperatorInspectionChoose.REPLACE_QUICKFIX_TITLE;
import static com.misset.opp.odt.inspection.operators.ODTOperatorInspectionChoose.USE_IIF;

class ODTOperatorInspectionChooseTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTOperatorInspectionChoose.class);
    }

    @Test
    void testChooseOperatorShowsNoWarning() {
        String content = insideQueryWithPrefixes("" +
                "CHOOSE WHEN $condition => 'a' WHEN NOT $condition => 'b' OTHERWISE => 'c' END");
        configureByText(content);
        assertNoWarning(USE_IIF);
    }

    @Test
    void testChooseOperatorShowsWarningOnSingleWhen() {
        String content = insideQueryWithPrefixes("" +
                "CHOOSE WHEN $condition => 'a' END");
        configureByText(content);
        assertHasWarning(USE_IIF);
    }

    @Test
    void testChooseOperatorShowsWarningOnSingleWhenWithOtherwise() {
        String content = insideQueryWithPrefixes("" +
                "CHOOSE WHEN $condition => 'a' OTHERWISE => 'b' END");
        configureByText(content);
        assertHasWarning(USE_IIF);
    }

    @Test
    void testChooseOperatorShowsErrorWhenNoEND() {
        String content = insideQueryWithPrefixes("" +
                "CHOOSE WHEN $condition => 'a' OTHERWISE => 'b'");
        configureByText(content);
        assertHasError(INCOMPLETE_CHOOSE_EXPECTED_END);
    }

    @Test
    void testChooseOperatorShowsErrorWhenNoWHEN() {
        String content = insideQueryWithPrefixes("" +
                "CHOOSE");
        configureByText(content);
        assertHasError(INCOMPLETE_CHOOSE_EXPECTED_WHEN_CONDITIONS);
    }

    @Test
    void testChooseOperatorShowsWeakWarningWhenOtherwiseIsNull() {
        String content = insideQueryWithPrefixes("" +
                "CHOOSE WHEN true => 'a' OTHERWISE => null END");
        configureByText(content);
        assertHasWeakWarning(OMIT_OTHERWISE);
    }

    @Test
    void testChooseOperatorRefactoresToIIFOnSingleWhen() {
        configureByText(insideQueryWithPrefixes("" +
                "CHOOSE WHEN $condition => 'a' END"));

        invokeQuickFixIntention(REPLACE_QUICKFIX_TITLE);
        Assertions.assertTrue(getFile().getText().contains("@IIF($condition, 'a')"));
        Assertions.assertFalse(getFile().getText().contains("CHOOSE"));
    }

    @Test
    void testChooseOperatorRefactoresToIIFWithOtherwiseOnSingleWhenWithOtherwise() {
        configureByText(insideQueryWithPrefixes("" +
                "CHOOSE WHEN $condition => 'a' OTHERWISE => 'b' END"));
        invokeQuickFixIntention(REPLACE_QUICKFIX_TITLE);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, 'a', 'b')"));
        Assertions.assertFalse(getFile().getText().contains("CHOOSE"));
    }

}
