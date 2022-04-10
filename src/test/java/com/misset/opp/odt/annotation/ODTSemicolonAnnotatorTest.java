package com.misset.opp.odt.annotation;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;

import static com.misset.opp.odt.annotation.ODTSemicolonAnnotator.*;

@RunWith(MockitoJUnitRunner.class)
class ODTSemicolonAnnotatorTest extends OMTInspectionTestCase {

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
        assertNoError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasNoErrorWhenNotAnOMTScriptMetaType() {
        String content = insideStandaloneQueryWithPrefixes("query: /ont:ClassA");
        configureByText(content);
        assertNoError(SEMICOLON_REQUIRED);
        assertNoError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasErrorWhenSemicolonAtQuery() {
        String content = insideStandaloneQueryWithPrefixes("query: /ont:ClassA;");
        configureByText(content);
        assertHasError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasErrorWhenVariableAssignment() {
        String content = insideProcedureRunWithPrefixes("VAR $test = 1");
        configureByText(content);
        assertHasError(SEMICOLON_REQUIRED);
    }

    @Test
    void testHasNoErrorWhenVariableAssignmentWithSemicolon() {
        String content = insideProcedureRunWithPrefixes("VAR $test = 1;");
        configureByText(content);
        assertNoError(SEMICOLON_REQUIRED);
        assertNoError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasNoErrorWhenNestedSignatureArgument() {
        String content = insideProcedureRunWithPrefixes("@LOG(@COMMAND());");
        configureByText(content);
        assertNoError(SEMICOLON_REQUIRED);
        assertNoError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasErrorWhenNestedSignatureArgumentHasSemicolon() {
        String content = insideProcedureRunWithPrefixes("@LOG(@COMMAND(););");
        configureByText(content);
        assertNoError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasNoErrorOnReturnStatement() {
        String content = insideProcedureRunWithPrefixes("RETURN @COMMAND(1);");
        configureByText(content);
        assertNoError(SEMICOLON_REQUIRED);
        assertNoError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasErrorOnReturnStatementWithoutSemicolon() {
        String content = insideProcedureRunWithPrefixes("RETURN @COMMAND(1)");
        configureByText(content);
        assertHasError(SEMICOLON_REQUIRED);
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
        assertNoError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasWarningWhenQueryStatementWithUnnecessarySemicolon() {
        configureByText(insideQueryWithPrefixesNoSemicolonEnding("1;;"));
        assertNoError(SEMICOLON_REQUIRED);
        assertNoError(SEMICOLON_ILLEGAL);
        assertHasWarning(SEMICOLON_UNNECESSARY);
    }

    @Test
    void testHasErrorWhenInterpolationWithUnnecessarySemicolon() {
        configureByText(insideQueryWithPrefixesNoSemicolonEnding("`${12;}`;"));
        assertNoError(SEMICOLON_REQUIRED);
        assertHasError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testNoErrorWhenInterpolationWithoutUnnecessarySemicolon() {
        configureByText(insideQueryWithPrefixesNoSemicolonEnding("`${12}`;"));
        assertNoError(SEMICOLON_REQUIRED);
        assertNoError(SEMICOLON_ILLEGAL);
    }

    @Test
    void testHasErrorWhenNoSemicolonInODTFile() {
        String content = "1";
        myFixture.configureByText("test.odt", content);
        assertHasError(SEMICOLON_REQUIRED);
    }

    @Test
    void testHasNoErrorWhenSemicolonInODTFile() {
        String content = "1;";
        myFixture.configureByText("test.odt", content);
        assertNoError(SEMICOLON_REQUIRED);
    }

}
