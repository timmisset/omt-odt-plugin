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
                "CHOOSE WHEN true => 'hi' WHEN false => 'bye' OTHERWISE => 'confused' END",
                "CHOOSE\n" +
                        "<indent>WHEN true => 'hi'\n" +
                        "<indent>WHEN false => 'bye'\n" +
                        "<indent>OTHERWISE => 'confused'\n" +
                        "END");
    }

    @Override
    public void assertFormatting(String before, String after) {
        after = getIndentedText(after);
        super.assertFormatting(before, after);
    }


}