package com.misset.opp.odt.annotation;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.annotation.ODTSemicolonAnnotator.SEMICOLON_ILLEGAL;
import static com.misset.opp.odt.annotation.ODTSemicolonAnnotator.SEMICOLON_REQUIRED;

class ODTSemicolonAnnotatorTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.emptyList();
    }

    @Test
    void testHasError() {
        String content = insideProcedureRunWithPrefixes("/ont:ClassA");
        configureByText(content);
        assertHasError(SEMICOLON_REQUIRED);
    }

    @Test
    void testHasNoError() {
        String content = insideProcedureRunWithPrefixes("/ont:ClassA;");
        configureByText(content);
        assertNoError(SEMICOLON_REQUIRED);
    }

    @Test
    void testHasNoErrorWhenNotAnOMTScriptMetaType() {
        String content = insideStandaloneQueryWithPrefixes("query: /ont:ClassA");
        configureByText(content);
        assertNoError(SEMICOLON_REQUIRED);
    }

    @Test
    void testHasErrorWhenSemicolonAtQuery() {
        String content = insideStandaloneQueryWithPrefixes("query: /ont:ClassA;");
        configureByText(content);
        assertHasError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasNoErrorWhenNestedAssignment() {
        String content = insideProcedureRunWithPrefixes("VAR $test = 1");
        configureByText(content);
        assertHasError(SEMICOLON_REQUIRED);
    }

    @Test
    void testHasNoErrorWhenNestedAssignmentWithSemicolon() {
        String content = insideProcedureRunWithPrefixes("VAR $test = 1;");
        configureByText(content);
        assertNoError(SEMICOLON_REQUIRED);
    }

    @Test
    void testHasErrorWhenQueryStatement() {
        configureByText(insideQueryWithPrefixesNoSemicolonEnding("1"));
        assertHasError(SEMICOLON_REQUIRED);
    }

    @Test
    void testHasNoErrorWhenQueryStatementWithSemicolon() {
        configureByText(insideQueryWithPrefixesNoSemicolonEnding("1;"));
        assertNoError(SEMICOLON_REQUIRED);
    }

}
