package com.misset.opp.odt.inspection.redundancy;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTUnusedDefineStatementsInspectionTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTUnusedDefineStatementsInspection.class);
    }

    @Test
    void getStaticDescription() {
        Assertions.assertEquals(ODTUnusedDefineStatementsInspection.DESCRIPTION, new ODTUnusedDefineStatementsInspection().getStaticDescription());
    }

    @Test
    void testHasWarningWhenUnusedCommand() {
        String content = "commands:\n" +
                "   DEFINE COMMAND command => { }\n";
        configureByText(content);
        assertHasWarning("command is never used");
    }

    @Test
    void testHasWarningWhenUnusedQuery() {
        String content = "queries:\n" +
                "   DEFINE QUERY query => '';\n";
        configureByText(content);
        assertHasWarning("query is never used");
    }

    @Test
    void testHasNoWarningWhenUsedCommand() {
        String content = "commands:\n" +
                "   DEFINE COMMAND command => { }\n" +
                "   DEFINE COMMAND command2 => { @command(); }";
        configureByText(content);
        assertNoWarning("command is never used");
    }

    @Test
    void testRemovesUnusedCommand() {
        String content = "commands:\n" +
                "   DEFINE COMMAND command => { }\n" +
                "   DEFINE COMMAND command2 => { }";
        configureByText(content);
        invokeQuickFixIntention("Remove command");

        Assertions.assertFalse(getFile().getText().contains("DEFINE COMMAND command => { }"));
    }

}
