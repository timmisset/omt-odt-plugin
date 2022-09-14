package com.misset.opp.omt;

import com.misset.opp.omt.inspection.unused.OMTUnusedImportMemberInspection;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class OMTImportOptimizerTest extends OMTTestCase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTUnusedImportMemberInspection.class));
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

        inspection.invokeQuickFixIntention("Optimize imports");

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
