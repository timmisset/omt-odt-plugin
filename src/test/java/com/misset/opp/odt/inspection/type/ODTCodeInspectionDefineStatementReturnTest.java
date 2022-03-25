package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTCodeInspectionDefineStatementReturnTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTCodeInspectionDefineStatementReturn.class);
    }

    @Test
    void testHasWarningWhenQueryIncompatibleTypes() {
        String content = withPrefixes("" +
                "queries: |\n" +
                "   /**\n" +
                "    * @param $paramA (string)\n" +
                "    * @return (integer)\n" +
                "    */\n" +
                "   DEFINE QUERY query($paramA) => '';\n");
        configureByText(content);
        assertHasWarning("Incompatible types");
    }

    @Test
    void testHasWarningWhenQueryVoid() {
        String content = withPrefixes("" +
                "queries: |\n" +
                "   /**\n" +
                "    * @param $paramA (string)\n" +
                "    * @return (void)\n" +
                "    */\n" +
                "   DEFINE QUERY query($paramA) => '';\n");
        configureByText(content);
        assertHasWarning("A query should not be typed void");
    }

    @Test
    void testHasWarningWhenCommandIncompatibleTypes() {
        String content = withPrefixes("" +
                "commands: |\n" +
                "   /**\n" +
                "    * @param $paramA (string)\n" +
                "    * @return (integer)\n" +
                "    */\n" +
                "   DEFINE COMMAND command($paramA) => {" +
                "       RETURN '';" +
                "   };\n");
        configureByText(content);
        assertHasWarning("Incompatible types");
    }

    @Test
    void testHasWarningWhenVoidCommandHasReturnStatements() {
        String content = withPrefixes("" +
                "commands: |\n" +
                "   /**\n" +
                "    * @param $paramA (string)\n" +
                "    * @return (void)\n" +
                "    */\n" +
                "   DEFINE COMMAND command($paramA) => {" +
                "       RETURN '';" +
                "   };\n");
        configureByText(content);
        assertHasWarning("A void command should not contain RETURN statements");
    }
}
