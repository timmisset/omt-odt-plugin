package com.misset.opp.odt.inspection.type;

import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ODTCodeInspectionDefineStatementReturnTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTCodeInspectionDefineStatementReturn.class));
    }

    @Test
    void testHasWarningWhenQueryIncompatibleTypes() {
        String content = withPrefixes("" +
                "/**\n" +
                " * @param $paramA (string)\n" +
                " * @return (integer)\n" +
                " */\n" +
                "DEFINE QUERY query($paramA) => '';\n");
        configureByText(content);
        inspection.assertHasWarning("Incompatible types");
    }

    @Test
    void testHasWarningWhenQueryVoid() {
        String content = withPrefixes("" +
                "/**\n" +
                " * @param $paramA (string)\n" +
                " * @return (void)\n" +
                " */\n" +
                "DEFINE QUERY query($paramA) => '';\n");
        configureByText(content);
        inspection.assertHasWarning("A query should not be typed void");
    }

    @Test
    void testHasWarningWhenCommandIncompatibleTypes() {
        String content = withPrefixes("" +
                "/**\n" +
                " * @param $paramA (string)\n" +
                " * @return (integer)\n" +
                " */\n" +
                "DEFINE COMMAND command($paramA) => {" +
                "    RETURN '';" +
                "};\n");
        configureByText(content);
        inspection.assertHasWarning("Incompatible types");
    }

    @Test
    void testHasWarningWhenVoidCommandHasReturnStatements() {
        String content = withPrefixes("" +
                "/**\n" +
                " * @param $paramA (string)\n" +
                " * @return (void)\n" +
                " */\n" +
                "DEFINE COMMAND command($paramA) => {" +
                "    RETURN '';" +
                "};\n");
        configureByText(content);
        inspection.assertHasWarning("A void command should not contain RETURN statements");
    }
}
