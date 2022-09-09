package com.misset.opp.odt.inspection.redundancy;

import com.misset.opp.odt.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ODTUnusedDefineStatementsInspectionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTUnusedDefineStatementsInspection.class));
    }

    @Test
    void getStaticDescription() {
        Assertions.assertEquals(ODTUnusedDefineStatementsInspection.DESCRIPTION, new ODTUnusedDefineStatementsInspection().getStaticDescription());
    }

    @Test
    void testHasWarningWhenUnusedCommand() {
        String content = "DEFINE COMMAND command => { }";
        configureByText(content);
        inspection.assertHasWarning("command is never used");
    }

    @Test
    void testHasWarningWhenUnusedQuery() {
        String content = "DEFINE QUERY query => '';\n";
        configureByText(content);
        inspection.assertHasWarning("query is never used");
    }

    @Test
    void testHasNoWarningWhenUsedCommand() {
        String content = "DEFINE COMMAND command => { }\n" +
                "   DEFINE COMMAND command2 => { @command(); }";
        configureByText(content);
        inspection.assertNoWarning("command is never used");
    }

    @Test
    void testRemovesUnusedCommand() {
        String content = "DEFINE COMMAND command => { }\n" +
                "   DEFINE COMMAND command2 => { }";
        configureByText(content);
        inspection.invokeQuickFixIntention("Remove command");

        Assertions.assertFalse(getFile().getText().contains("DEFINE COMMAND command => { }"));
    }

}
