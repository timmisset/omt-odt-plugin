package com.misset.opp.omt;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.omt.inspection.unused.OMTUnusedImportMemberInspection;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class OMTImportOptimizerTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(OMTUnusedImportMemberInspection.class);
    }

    @Test
    void testRemovesUnusedImports() {
        addFileToProject("test.omt", "commands:\n" +
                "   DEFINE COMMAND unusedMember => {}\n" +
                "   DEFINE COMMAND usedMember => {}");
        String content = "import:\n" +
                "   ./test.omt:\n" +
                "   - unusedMember\n" +
                "   - usedMember\n" +
                "\n" +
                "model:\n" +
                "   Procedure: !Procedure\n" +
                "       onRun: |\n" +
                "           @usedMember();";
        OMTFile omtFile = configureByText(content);

        invokeQuickFixIntention("Optimize imports");

        Assertions.assertEquals("import:\n" +
                "   ./test.omt:\n" +
                "     - usedMember\n" +
                "\n" +
                "model:\n" +
                "   Procedure: !Procedure\n" +
                "       onRun: |\n" +
                "           @usedMember();", omtFile.getText());
    }

}
