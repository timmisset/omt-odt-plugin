package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTStyleInspectionUnnecessaryTrueConditionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTStyleInspectionUnnecessaryTrueCondition.class);
    }

    @Test
    void testShowsWarningWhenUnnecessaryTrueConditionOnLeftside() {
        String content = insideProcedureRunWithPrefixes("IF true == $x { }");
        configureByText(content);
        assertHasWarning("true == $x can be simplified to $x");
    }

    @Test
    void testShowsWarningWhenUnnecessaryTrueConditionOnRightside() {
        String content = insideProcedureRunWithPrefixes("IF $x == true { }");
        configureByText(content);
        assertHasWarning("$x == true can be simplified to $x");
    }

    @Test
    void testShowsWarningWhenInsideQuery() {
        String content = insideQueryWithPrefixes("$x == true AND $y == true");
        configureByText(content);
        assertHasWarning("$x == true can be simplified to $x");
        assertHasWarning("$y == true can be simplified to $y");
    }

    @Test
    void testShowsNoWarningWhenInsideBooleanOperator() {
        String content = insideQueryWithPrefixes("AND($x == true, $y == true)");
        configureByText(content);
        assertNoWarning("$x == true can be simplified to $x");
        assertNoWarning("$y == true can be simplified to $y");
    }

    @Test
    void testAppliesQuickfix() {
        configureByText(insideProcedureRunWithPrefixes("IF $x == true { }"));
        invokeQuickFixIntention("Replace with: $x");
        Assertions.assertFalse(getFile().getText().contains("IF $x == true { }"));
        Assertions.assertTrue(getFile().getText().contains("IF $x { }"));
    }
}
