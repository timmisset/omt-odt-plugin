package com.misset.opp.odt.formatter;

import com.misset.opp.odt.testcase.ODTFormattingTestCase;
import org.junit.jupiter.api.Test;

class ODTFormattingSpacingTest extends ODTFormattingTestCase {

    @Test
    void testLineFeedBetweenScriptLines() {
        assertFormatting("VAR $variableA;VAR $variableB;",
                "VAR $variableA;\nVAR $variableB;");
    }

    @Test
    void testCommandBlockAddsLineFeeds() {
        assertFormatting("{ @LOG('hi') }", getIndentedText("{\n" +
                "<indent>@LOG('hi')\n" +
                "}"));
    }

    @Test
    void testCommandBlockPutOpeningCurlyOnFirstLine() {
        assertFormatting("IF true\n" +
                "{ @LOG('hi') }", getIndentedText("IF true {\n" +
                "<indent>@LOG('hi')\n" +
                "}"));
    }

    @Test
    void testIfOperatorSpaceForQuery() {
        assertFormatting("IF(true)", "IF (true)");
    }

    @Test
    void testIfElse() {
        assertFormatting("IF(true) { @LOG('if'); } ELSE { @LOG('else'); }", getIndentedText("IF (true) {\n" +
                "<indent>@LOG('if');\n" +
                "} ELSE {\n" +
                "<indent>@LOG('else');\n" +
                "}"));
    }

    @Test
    void testIfElseIfElse() {
        assertFormatting("IF(true) { @LOG('if'); } " +
                        "ELSE IF false { @LOG('else if'); }" +
                        "ELSE { @LOG('else'); }",
                getIndentedText("IF (true) {\n" +
                        "<indent>@LOG('if');\n" +
                        "} ELSE IF false {\n" +
                        "<indent>@LOG('else if');\n" +
                        "} ELSE {\n" +
                        "<indent>@LOG('else');\n" +
                        "}"));
    }

    @Test
    void testSpacingAroundQueryPathSeparator() {
        assertFormatting("VAR $variableA = /ont:Class/ont:somePath;",
                "VAR $variableA = /ont:Class / ont:somePath;");
    }

    @Test
    void testSpacingAroundLambda() {
        assertFormatting("DEFINE QUERY query=>'hi';",
                "DEFINE QUERY query => 'hi';");
    }

    @Test
    void testSignatureArgumentAddsSpaces() {
        assertFormatting("Call('hi','bye');",
                "Call('hi', 'bye');");
    }

    @Test
    void testSignatureArgumentQueryAddsSpaces() {
        assertFormatting("Call($variableA,$variableB);",
                "Call($variableA, $variableB);");
    }

    @Test
    void testSignatureArgumentRemovesSpaces() {
        assertFormatting("Call( 'hi' , 'bye' );",
                "Call('hi', 'bye');");
    }

    @Test
    void testSignatureArgumentQueryRemovesSpaces() {
        assertFormatting("Call( $variableA , $variableB );",
                "Call($variableA, $variableB);");
    }

    @Test
    void testSignatureFlagAndParentheses() {
        assertFormatting("Call!flag ($variableA);",
                "Call!flag($variableA);");
    }

    @Test
    void testCallnameAndFlag() {
        assertFormatting("Call !flag($variableA);",
                "Call!flag($variableA);");
    }

    @Test
    void testCallSymbolAndName() {
        assertFormatting("@ Call($variableA);",
                "@Call($variableA);");
    }

    @Test
    void testAssignmentOperator() {
        assertFormatting("$variable='hi';",
                "$variable = 'hi';");
    }

    @Test
    void testAddToOperator() {
        assertFormatting("$variable+='hi';",
                "$variable += 'hi';");
    }

    @Test
    void testEqualsOperator() {
        assertFormatting("$variable=='hi';",
                "$variable == 'hi';");
    }

    @Test
    void testJavaDocsBlock() {
        assertFormatting(
                "/**\n" +
                        "* SomeInfo\n" +
                        "*/\n",
                "/**\n" +
                        " * SomeInfo\n" +
                        " */\n");
    }

}
