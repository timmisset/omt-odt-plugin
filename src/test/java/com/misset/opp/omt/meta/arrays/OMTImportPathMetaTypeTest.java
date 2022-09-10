package com.misset.opp.omt.meta.arrays;

import com.intellij.application.options.CodeStyle;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.jetbrains.yaml.formatter.YAMLCodeStyleSettings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.misset.opp.omt.meta.arrays.OMTImportPathMetaType.SORT_MEMBERS;

class OMTImportPathMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Set.of(OMTValueInspection.class, OMTUnkownKeysInspection.class));
    }

    @Test
    void testShowsUnsortedWarning() {
        String content = "import:\n" +
                "   ./a-file.omt:\n" +
                "   - memberB\n" +
                "   - memberA\n";
        configureByText(content);
        inspection.assertHasWeakWarning(OMTImportPathMetaType.UNSORTED_IMPORT_MEMBERS);
    }

    @Test
    void testShowsNoUnsortedWarning() {
        String content = "import:\n" +
                "   ./a-file.omt:\n" +
                "   - memberA\n" +
                "   - memberB\n";
        configureByText(content);
        inspection.assertNoWeakWarning(OMTImportPathMetaType.UNSORTED_IMPORT_MEMBERS);
    }

    @Test
    void testSortsTheUnsortedMembers() {

        String content = "import:\n" +
                "   ./a-file.omt:\n" +
                "   - memberB\n" +
                "   - memberA\n";
        configureByText(content);

        final YAMLCodeStyleSettings customSettings = CodeStyle.getCustomSettings(getFile(),
                YAMLCodeStyleSettings.class);
        customSettings.INDENT_SEQUENCE_VALUE = false;

        inspection.invokeQuickFixIntention(SORT_MEMBERS);

        Assertions.assertEquals("import:\n" +
                "   ./a-file.omt:\n" +
                "   - memberA\n" +
                "   - memberB\n", getFile().getText());
    }

}
