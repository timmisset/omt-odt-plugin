package com.misset.opp.odt.inspection.calls.operators;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.odt.inspection.calls.operators.ODTOperatorInspectionChoose.*;

class ODTOperatorInspectionChooseTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTOperatorInspectionChoose.class));
    }

    @Test
    void testChooseOperatorShowsNoWarning() {
        String content = "CHOOSE WHEN $condition => 'a' WHEN NOT $condition => 'b' OTHERWISE => 'c' END";
        configureByText(content);
        inspection.assertNoWarning(USE_IIF);
    }

    @Test
    void testChooseOperatorShowsWarningOnSingleWhen() {
        String content = "CHOOSE WHEN $condition => 'a' END";
        configureByText(content);
        inspection.assertHasWarning(USE_IIF);
    }

    @Test
    void testChooseOperatorShowsWarningOnSingleWhenWithOtherwise() {
        String content = "CHOOSE WHEN $condition => 'a' OTHERWISE => 'b' END";
        configureByText(content);
        inspection.assertHasWarning(USE_IIF);
    }

    @Test
    void testChooseOperatorShowsErrorWhenNoEND() {
        String content = "CHOOSE WHEN $condition => 'a' OTHERWISE => 'b'";
        configureByText(content);
        inspection.assertHasError(INCOMPLETE_CHOOSE_EXPECTED_END);
    }

    @Test
    void testChooseOperatorShowsErrorWhenNoWHEN() {
        String content = "CHOOSE";
        configureByText(content);
        inspection.assertHasError(INCOMPLETE_CHOOSE_EXPECTED_WHEN_CONDITIONS);
    }

    @Test
    void testChooseOperatorShowsWeakWarningWhenOtherwiseIsNull() {
        String content = "CHOOSE WHEN true => 'a' OTHERWISE => null END";
        configureByText(content);
        inspection.assertHasWeakWarning(OMIT_OTHERWISE);
    }

    @Test
    void testChooseOperatorRefactoresToIIFOnSingleWhen() {
        configureByText("CHOOSE WHEN $condition => 'a' END");

        inspection.invokeQuickFixIntention(REPLACE_QUICKFIX_TITLE);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, 'a')"));
        Assertions.assertFalse(getFile().getText().contains("CHOOSE"));
    }

    @Test
    void testChooseOperatorRefactoresToIIFWithOtherwiseOnSingleWhenWithOtherwise() {
        configureByText("CHOOSE WHEN $condition => 'a' OTHERWISE => 'b' END");
        inspection.invokeQuickFixIntention(REPLACE_QUICKFIX_TITLE);
        Assertions.assertTrue(getFile().getText().contains("IIF($condition, 'a', 'b')"));
        Assertions.assertFalse(getFile().getText().contains("CHOOSE"));
    }

}
