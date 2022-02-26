package com.misset.opp.odt;

import com.intellij.lang.folding.FoldingDescriptor;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.testCase.ODTTestCase;
import org.junit.jupiter.api.Test;

class ODTFoldingBuilderTest extends ODTTestCase {

    ODTFoldingBuilder foldingBuilder = new ODTFoldingBuilder();

    @Test
    void testHasFoldingCommandBlock() {
        String content = "IF true {\n" +
                "   @LOG('test');\n" +
                "}";
        ODTFile odtFile = configureByText(content);
        FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegions(odtFile.getNode(), getEditor().getDocument());
        System.out.println(foldingDescriptors);
    }

}
