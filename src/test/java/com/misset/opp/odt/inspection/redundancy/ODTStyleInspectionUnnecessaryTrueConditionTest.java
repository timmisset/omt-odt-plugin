package com.misset.opp.odt.inspection.redundancy;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ODTStyleInspectionUnnecessaryTrueConditionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTStyleInspectionUnnecessaryTrueCondition.class));
    }

    @Test
    void testShowsWarningWhenUnnecessaryTrueConditionOnLeftside() {
        configureByText("IF true == $x { }");
        inspection.assertHasWarning("true == $x can be simplified to $x");
    }

    @Test
    void testShowsWarningWhenUnnecessaryTrueConditionOnRightside() {
        configureByText("IF $x == true { }");
        inspection.assertHasWarning("$x == true can be simplified to $x");
    }

    @Test
    void testShowsWarningWhenInsideQuery() {
        configureByText("$x == true AND $y == true");
        inspection.assertHasWarning("$x == true can be simplified to $x");
        inspection.assertHasWarning("$y == true can be simplified to $y");
    }

    @Test
    void testShowsNoWarningWhenInsideBooleanOperator() {
        configureByText("AND($x == true, $y == true)");
        inspection.assertNoWarning("$x == true can be simplified to $x");
        inspection.assertNoWarning("$y == true can be simplified to $y");
    }

    @Test
    void testAppliesQuickfix() {
        configureByText("IF $x == true { }");
        inspection.invokeQuickFixIntention("Replace with: $x");
        assertEquals("IF $x { }", getFile().getText());
    }
}
