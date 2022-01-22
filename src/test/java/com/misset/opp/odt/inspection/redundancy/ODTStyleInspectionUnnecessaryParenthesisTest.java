package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.inspection.redundancy.ODTStyleInspectionUnnecessaryParenthesis.WARNING;

class ODTStyleInspectionUnnecessaryParenthesisTest extends OMTInspectionTestCase {


    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTStyleInspectionUnnecessaryParenthesis.class);
    }

    @Test
    void testShowsWarningWhenUnnecessaryEmptyOperatorCall() {
        String content = insideProcedureRunWithPrefixes("LOG();");
        configureByText(content);
        assertHasWarning(WARNING);
    }

    @Test
    void testRemovesParenthesis() {
        String content = insideProcedureRunWithPrefixes("LOG();");
        configureByText(content);
        invokeQuickFixIntention("Remove parenthesis");
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
    void testRemovesParenthesisForDefineQueryStatement() {
        String content = insideProcedureRunWithPrefixes("DEFINE QUERY query() => '';");
        configureByText(content);
        invokeQuickFixIntention("Remove parenthesis");
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
        invokeQuickFixIntention("Remove parenthesis");
        Assertions.assertFalse(getFile().getText().contains("DEFINE COMMAND command() => { }"));
        Assertions.assertTrue(getFile().getText().contains("DEFINE COMMAND command => { }"));
    }
}
