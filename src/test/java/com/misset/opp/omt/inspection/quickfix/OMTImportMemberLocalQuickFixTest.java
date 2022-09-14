package com.misset.opp.omt.inspection.quickfix;

import com.intellij.application.options.CodeStyle;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ex.ProblemDescriptorImpl;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.util.TextRange;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.testcase.OMTTestCase;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.yaml.formatter.YAMLCodeStyleSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OMTImportMemberLocalQuickFixTest extends OMTTestCase {

    private void setYamlIndentation(OMTFile file) {
        YAMLCodeStyleSettings yamlCodeStyleSettings = CodeStyle.getCustomSettings(file, YAMLCodeStyleSettings.class);
        yamlCodeStyleSettings.INDENT_SEQUENCE_VALUE = false;
    }

    @Test
    void testAddsImportToFile() {
        OMTFile omtFile = addFileToProject("importedFile.omt", "queries: |\n" +
                "   DEFINE QUERY query => '';");
        String content = "queries:\n" +
                "   DEFINE QUERY anotherQuery => <caret>query;\n";
        OMTFile importingFile = configureByText("importingFile.omt", content);

        setYamlIndentation(importingFile);

        ProblemDescriptorImpl descriptor = ReadAction.compute(() -> {
            ODTCall call = (ODTCall) myFixture.getElementAtCaret();
            PsiCallable callable = omtFile.getExportingMembersMap().get("query").iterator().next();
            OMTImportMemberLocalQuickFix localQuickFix = new OMTImportMemberLocalQuickFix(omtFile, callable);

            Assertions.assertEquals("Import as DEFINE QUERY from ./importedFile.omt", localQuickFix.getName());
            Assertions.assertEquals("Import", localQuickFix.getFamilyName());

            return new ProblemDescriptorImpl(call,
                    call,
                    "message",
                    new LocalQuickFix[]{localQuickFix},
                    ProblemHighlightType.ERROR,
                    false,
                    TextRange.EMPTY_RANGE,
                    false
            );
        });
        WriteCommandAction.runWriteCommandAction(getProject(), () -> descriptor.getFixes()[0].applyFix(getProject(), descriptor));

        ReadAction.run(() -> {
            String text = importingFile.getText();
            Assertions.assertEquals("import:\n" +
                    "  ./importedFile.omt:\n" +
                    "  - query\n" +
                    "queries:\n" +
                    "   DEFINE QUERY anotherQuery => query;\n", text);
        });

    }

}
