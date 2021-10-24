package com.misset.opp.omt.psi.references;

import com.intellij.openapi.application.ReadAction;
import com.intellij.usageView.UsageInfo;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;

class OMTImportMemberReferenceTest extends OMTTestCase {

    @Test
    void testImportReferenceHasReferenceToQuery() {
        configureByText("importedFile.omt", "" +
                "queries:\n" +
                "   DEFINE QUERY query => '';");
        configureByText("" +
                "import:\n" +
                "   ./importedFile.omt:\n" +
                "   - query\n" +
                "");

        ReadAction.run(() -> {
            final YAMLPlainTextImpl query = myFixture.findElementByText("query", YAMLPlainTextImpl.class);
            Assertions.assertTrue(query.getReference().resolve() instanceof ODTDefineName);
        });
    }

    @Test
    void testImportReferenceHasReferenceToCommand() {
        configureByText("importedFile.omt", "" +
                "commands:\n" +
                "   DEFINE COMMAND command => { }");
        configureByText("" +
                "import:\n" +
                "   ./importedFile.omt:\n" +
                "   - command\n" +
                "");
        ReadAction.run(() -> {
            final YAMLPlainTextImpl command = myFixture.findElementByText("command", YAMLPlainTextImpl.class);
            Assertions.assertTrue(command.getReference().resolve() instanceof ODTDefineName);
        });
    }

    @Test
    void testCanFindUsage() {
        configureByText("" +
                "import:\n" +
                "   ./importedFile.omt:\n" +
                "   - co<caret>mmand\n" +
                "");
        configureByText("importedFile.omt", "" +
                "commands:\n" +
                "   DEFINE COMMAND comm<caret>and => { }");

        withProgress(() -> ReadAction.run(() -> {
            final ODTDefineName defineName = (ODTDefineName) myFixture.getElementAtCaret();
            final Collection<UsageInfo> usages = myFixture.findUsages(defineName);
            Assertions.assertEquals(1, usages.size());
            Assertions.assertTrue(usages.stream().findFirst().get().getReference() instanceof OMTImportMemberReference);
        }));
    }
}
