package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.InspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

class ODTCodeUntypedInspectionWarningTest extends InspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Set.of(ODTCodeUntypedInspectionWarning.class);
    }

    @Test
    void testHasWarningWhenNotAnnotated() {
        configureByText("queries: |\n" +
                "   DEFINE QUERY query($param) => '';\n");
        assertHasWarning(ODTCodeUntypedInspectionWarning.WARNING);
    }

    @Test
    void testNoWarningWhenNotAnnotated() {
        configureByText("queries: |\n" +
                "   /**\n" +
                "    * @param $param (string)\n" +
                "    */\n" +
                "   DEFINE QUERY query($param) => '';\n");
        assertNoWarning(ODTCodeUntypedInspectionWarning.WARNING);
    }

    @Test
    void testAddsJavaDoc() {
        configureByText("queries:\n" +
                "   DEFINE QUERY query($param) => '';\n");
        invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.WARNING);
        Assertions.assertEquals("queries:\n" +
                "  /**\n" +
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param) => '';\n", getFile().getText());
    }

    @Test
    void testAddsJavaDocPersistComment() {
        configureByText("queries:\n" +
                "  /**\n" +
                "   * There's something about query\n" +
                "   */\n" +
                "   DEFINE QUERY query($param) => '';\n");
        invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.WARNING);
        Assertions.assertEquals("queries:\n" +
                "  /**\n" +
                "   * There's something about query\n" +
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param) => '';\n", getFile().getText());
    }

    @Test
    void testAddsJavaDocAppendToExistingBlock() {
        configureByText("queries:\n" +
                "  /**\n" +
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param2, $param) => '';\n");
        invokeQuickFixIntention(ODTCodeUntypedInspectionWarning.WARNING);
        Assertions.assertEquals("queries:\n" +
                "  /**\n" +
                "   * @param $param2 (TypeOrClass)\n" + // <-- ordered according to DefineParam ordering
                "   * @param $param (TypeOrClass)\n" +
                "   */\n" +
                "  DEFINE QUERY query($param2, $param) => '';\n", getFile().getText());
    }
}