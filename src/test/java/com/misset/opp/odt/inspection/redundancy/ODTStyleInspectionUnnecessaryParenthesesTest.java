package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.redundancy.ODTStyleInspectionUnnecessaryParentheses.WARNING;

class ODTStyleInspectionUnnecessaryParenthesesTest extends OMTInspectionTestCase {


    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTStyleInspectionUnnecessaryParentheses.class);
    }

    @Test
    void testShowsWarningWhenUnnecessaryEmptyOperatorCall() {
        String content = insideProcedureRunWithPrefixes("LOG();");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testRemovesParentheses() {
        String content = insideProcedureRunWithPrefixes("LOG();");
        configureByText(content);
        invokeQuickFixIntention("Remove parentheses");
        Assertions.assertFalse(getFile().getText().contains("LOG();"));
        Assertions.assertTrue(getFile().getText().contains("LOG;"));
    }

    @Test
    void testShowsNoWarningWhenOperatorCallWithArguments() {
        String content = insideProcedureRunWithPrefixes("LOG('test');");
        configureByText(content);
        assertNoWarning(WARNING);
    }

    @Test
    void testShowsNoWarningWhenEmptyCommandCall() {
        String content = insideProcedureRunWithPrefixes("@LOG();");
        configureByText(content);
        assertNoWarning(WARNING);
    }

    @Test
    void testShowsWarningForDefineQueryStatementWithoutArguments() {
        String content = insideProcedureRunWithPrefixes("DEFINE QUERY query() => '';");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testRemovesParenthesesForDefineQueryStatement() {
        String content = insideProcedureRunWithPrefixes("DEFINE QUERY query() => '';");
        configureByText(content);
        invokeQuickFixIntention("Remove parentheses");
        Assertions.assertFalse(getFile().getText().contains("DEFINE QUERY query() => '';"));
        Assertions.assertTrue(getFile().getText().contains("DEFINE QUERY query => '';"));
    }

    @Test
    void testShowsWarningForDefineCommandStatementWithoutArguments() {
        String content = insideProcedureRunWithPrefixes("DEFINE COMMAND command() => { }");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testRemovesParenthesisForDefineCommandStatement() {
        String content = insideProcedureRunWithPrefixes("DEFINE COMMAND command() => { }");
        configureByText(content);
        invokeQuickFixIntention("Remove parentheses");
        Assertions.assertFalse(getFile().getText().contains("DEFINE COMMAND command() => { }"));
        Assertions.assertTrue(getFile().getText().contains("DEFINE COMMAND command => { }"));
    }
}
