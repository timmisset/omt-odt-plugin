package com.misset.opp.omt;

import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.YAMLElementTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * The necessity for the OMTFoldingBuilder is due to the bug reported at:
 * https://youtrack.jetbrains.com/issue/IDEA-289722
 * <p>
 * Once this bug is fixed the OMTFolding, which should be agnostic to the language of the injected fragment
 * can be removed. For now, it will force additional folding regions from injected fragments
 */
class OMTFoldingBuilderTest extends OMTTestCase {

    @Test
    void testFoldsYamlBlocksAndInjectedFragments() {
        OMTFoldingBuilder foldingBuilder = new OMTFoldingBuilder();

        String content = "commands: |\n" +
                "   DEFINE COMMAND command => {\n" +
                "       @LOG('test');\n" +
                "   }";
        OMTFile omtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegions(omtFile.getNode(), getEditor().getDocument());
            Assertions.assertEquals(2, foldingDescriptors.length);
            FoldingDescriptor yamlFolding = foldingDescriptors[0];
            FoldingDescriptor odtFolding = foldingDescriptors[1];

            Assertions.assertEquals(YAMLElementTypes.SCALAR_LIST_VALUE, yamlFolding.getElement().getElementType());
            Assertions.assertEquals(ODTTypes.COMMAND_BLOCK, odtFolding.getElement().getElementType());

            Assertions.assertTrue(yamlFolding.getRange().contains(odtFolding.getRange()));
        });
    }
}
