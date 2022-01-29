package com.misset.opp.odt.formatter;

import com.misset.opp.testCase.ODTFormattingTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ODTFormattingIndentTest extends ODTFormattingTestCase {

    @Test
    void testNoIndentOnNewLine() {
        configureByText("VAR $test;<caret>");
        enter();
        Assertions.assertEquals("VAR $test;\n", getDocumentText());
    }

    @Test
    void testNoIndentOnNewLineWithTrailing() {
        configureByText("VAR $test;<caret>\n" +
                "VAR $test2;");
        enter();
        Assertions.assertEquals("VAR $test;\n\nVAR $test2;", getDocumentText());
    }

    @Test
    void testIndentOnNewLineWithTrailingWhenIncomplete() {
        configureByText("VAR $test = <caret>'test';\n" +
                "VAR $test2;");
        enter();
        Assertions.assertEquals(getIndentedText("VAR $test = \n<indent>'test';\nVAR $test2;"), getDocumentText());
    }

    @Test
    void testHasIndentOnNestedNewLine() {
        configureByText("IF true {<caret>\n" +
                "}");
        enter();
        Assertions.assertEquals(getIndentedText("IF true {\n" +
                "<indent>\n" +
                "}"), getDocumentText());
    }

    @Test
    void testHasIndentOnNestedFilterNewLine() {
        configureByText("IF true [<caret>\n");
        enter();
        Assertions.assertEquals(getIndentedText("IF true [\n" +
                "<indent>\n"), getDocumentText());
    }

    @Test
    void testHasIndentOnNestedParenthesesNewLine() {
        configureByText("IF (<caret>\n");
        enter();
        Assertions.assertEquals(getIndentedText("IF (\n" +
                "<indent>\n"), getDocumentText());
    }

    @Test
    void testHasIndentOnNestedNewLineWithContent() {
        configureByText(getIndentedText("IF true {\n" +
                "<indent>VAR $x;<caret>\n" +
                "}"));
        enter();
        Assertions.assertEquals(getIndentedText("IF true {\n" +
                "<indent>VAR $x;\n" +
                "<indent>\n" +
                "}"), getDocumentText());
    }

    @Test
    void testNoIndentationOnSecondLine() {
        assertFormatting("PREFIX ont: <http://ontology#>;VAR $test = '1';",
                "PREFIX ont: <http://ontology#>;\n" +
                        "VAR $test = '1';");
    }

    @Test
    void testIndentScriptBlock() {
        assertFormatting("IF true {\n" +
                        "@LOG('hi');\n" +
                        "}",
                "IF true {\n" +
                        "<indent>@LOG('hi');\n" +
                        "}");
    }

    @Test
    void testIndentQueryStatement() {
        assertFormatting("DEFINE QUERY query =>\n" +
                        "$someValue;",
                "DEFINE QUERY query =>\n" +
                        "<indent>$someValue;");
    }

    @Test
    void testIndentQueryStatementBrokenStatement() {
        assertFormatting("DEFINE QUERY query => $someValue /\n" +
                        "LOG;",
                "DEFINE QUERY query => $someValue /\n" +
                        "<indent>LOG;");
    }

    @Test
    void testIndentQueryStatementBrokenStatementWithOtherOperators() {
        assertFormatting("DEFINE QUERY query => $someValue\n" +
                        "AND $anotherValue AND\n" +
                        "NOT $theLastValue;",
                "DEFINE QUERY query => $someValue\n" +
                        "<indent>AND $anotherValue AND\n" +
                        "<indent>NOT $theLastValue;");
    }

    @Test
    void testIndentQueryStatementBrokenStatementOnPathSeperator() {
        assertFormatting("DEFINE QUERY query => $someValue\n" +
                        "/ LOG;",
                "DEFINE QUERY query => $someValue\n" +
                        "<indent>/ LOG;");
    }

    @Test
    void testChooseBlock() {
        assertFormatting(
                "DEFINE QUERY query => \n" +
                        "CHOOSE WHEN true => 'hi' WHEN false => 'bye' OTHERWISE => 'confused' END",
                "DEFINE QUERY query =>\n" +
                        "    CHOOSE\n" +
                        "        WHEN true => 'hi'\n" +
                        "        WHEN false => 'bye'\n" +
                        "        OTHERWISE => 'confused'\n" +
                        "    END");
    }

    @Test
    void testMultilineQuery() {
        assertFormatting("$variable /\nLOG",
                "$variable /\n<indent>LOG");
    }

    @Test
    void testFormatEOLComments() {
        assertFormatting("IF true {\n" +
                "      # some comment\n" +
                "   @LOG('here');\n" +
                "}", "IF true {\n" +
                "    # some comment\n" +
                "    @LOG('here');\n" +
                "}");
    }

    @Test
    void testMultilineScriptNoIndents() {
        assertCorrectFormatting("VAR $variableA;\n" +
                "VAR $variableB;\n" +
                "VAR $variableC;");
    }

    @Test
    void testMultilineScriptInsideCommand() {
        assertCorrectFormatting("{\n" +
                "<indent>VAR $variableA;\n" +
                "<indent>VAR $variableB;\n" +
                "<indent>VAR $variableC;\n" +
                "}");
    }

    @Test
    void testMultilineScriptLineInsideFilter() {
        assertCorrectFormatting("$variable[\n" +
                "<indent>true\n" +
                "]");
    }

    @Test
    void testMultilineScriptLineInsideParentheses() {
        assertCorrectFormatting("@CALL(\n" +
                "<indent>true\n" +
                ")");
    }

    @Test
    void testMultilineMultipleArgumentsLineUp() {
        assertCorrectFormatting("@CALL(\n" +
                "<indent>true,\n" +
                "<indent>false,\n" +
                "<indent>1\n" +
                ")");
    }

    @Test
    void testMultiNestedCommand() {
        assertCorrectFormatting("IF true {\n" +
                "<indent>IF false {\n" +
                "<indent><indent>VAR $variableA\n" +
                "<indent>}\n" +
                "<indent>VAR $variableB;\n" +
                "<indent>VAR $variableC;\n" +
                "}");
    }

    @Test
    void testDocComment() {
        assertCorrectFormatting("/**\n" +
                " * @base (TypeOrClass)\n" +
                " * @param $param2 (TypeOrClass)\n" +
                " * @param $param (TypeOrClass)\n" +
                " */\n" +
                "DEFINE QUERY query($param2, $param) => ont:booleanPredicate;");
    }

    @Test
    void testEOLComment() {
        assertCorrectFormatting("# Some random text\n" +
                "/**\n" +
                " * @param $paramA (ont:ClassA)\n" +
                " */\n" +
                "DEFINE QUERY query => $param\n" +
                "<indent>/ EXISTS;");
    }

    @Override
    public void assertFormatting(String before, String after) {
        after = getIndentedText(after);
        super.assertFormatting(before, after);
    }

    private void assertCorrectFormatting(String formatted) {
        String indentedText = getIndentedText(formatted);
        super.assertFormatting(indentedText, indentedText);
    }


}
