package com.misset.opp.odt;

import com.intellij.codeInsight.folding.JavaCodeFoldingSettings;
import com.intellij.lang.ASTNode;
import com.intellij.lang.folding.FoldingDescriptor;
import com.intellij.openapi.application.ReadAction;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.testCase.ODTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ODTFoldingBuilderTest extends ODTTestCase {
    ODTFoldingBuilder foldingBuilder = new ODTFoldingBuilder();

    @Test
    void testHasFoldingCommandBlockSmallBlock() {
        String content = "IF true {\n" +
                "   @LOG('test');\n" +
                "}";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegionsWithOffset(odtFile, 0);
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];
            ASTNode node = foldingDescriptor.getElement();
            Assertions.assertEquals(ODTTypes.COMMAND_BLOCK, node.getElementType());
            Assertions.assertEquals("{ @LOG('test'); }", foldingDescriptor.getPlaceholderText());
            Assertions.assertFalse(foldingBuilder.isCollapsedByDefault(node));
        });
    }

    @Test
    void testHasFoldingCommandBlockLargeBlock() {
        String content = "IF true {\n" +
                "   @LOG('testing with a large block');\n" +
                "}";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            // use super buildFoldRegions method to test the ODT-only flow
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegions(odtFile.getNode(), myFixture.getDocument(odtFile));
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];
            ASTNode node = foldingDescriptor.getElement();
            Assertions.assertEquals(ODTTypes.COMMAND_BLOCK, node.getElementType());
            Assertions.assertEquals("{...}", foldingDescriptor.getPlaceholderText());
        });
    }

    @Test
    void testIncludesOffsetInResult() {
        String content = "IF true {\n" +
                "   @LOG('testing with a large block');\n" +
                "}";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegionsWithOffset(odtFile, 0);
            int withoutOffset = foldingDescriptors[0].getRange().getStartOffset();

            foldingDescriptors = foldingBuilder.buildFoldRegionsWithOffset(odtFile, 5);
            int withOffset = foldingDescriptors[0].getRange().getStartOffset();

            Assertions.assertEquals(withOffset, withoutOffset + 5);
        });
    }

    @Test
    void testDefineCommandFoldsCodeBlock() {
        String content = "DEFINE COMMAND command => {\n" +
                "   @LOG('testing with a large block');\n" +
                "}";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegionsWithOffset(odtFile, 0);
            Assertions.assertEquals(1, foldingDescriptors.length);
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];
            Assertions.assertEquals("{...}", foldingDescriptor.getPlaceholderText());
            Assertions.assertEquals("{\n" +
                    "   @LOG('testing with a large block');\n" +
                    "}", foldingDescriptor.getElement().getText());
        });
    }

    @Test
    void testDefineQueryFoldsQueryPath() {
        String content = "DEFINE QUERY query => true / false / true / false;";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegionsWithOffset(odtFile, 0);
            Assertions.assertEquals(1, foldingDescriptors.length);
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];
            Assertions.assertEquals("...", foldingDescriptor.getPlaceholderText());
            Assertions.assertEquals("true / false / true / false", foldingDescriptor.getElement().getText());
        });
    }

    @Test
    void testDefineQueryFoldsQueryArray() {
        String content = "DEFINE QUERY query => true | false | true | false;";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegionsWithOffset(odtFile, 0);
            Assertions.assertEquals(1, foldingDescriptors.length);
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];
            Assertions.assertEquals("...", foldingDescriptor.getPlaceholderText());
            Assertions.assertEquals("true | false | true | false", foldingDescriptor.getElement().getText());
        });
    }

    @Test
    void testDefineQueryFoldsEquationStatement() {
        String content = "DEFINE QUERY query => true == false;";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegionsWithOffset(odtFile, 0);
            Assertions.assertEquals(1, foldingDescriptors.length);
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];
            Assertions.assertEquals("...", foldingDescriptor.getPlaceholderText());
            Assertions.assertEquals("true == false", foldingDescriptor.getElement().getText());
        });
    }

    @Test
    void testDefineQueryFoldsBooleanStatement() {
        String content = "DEFINE QUERY query => true OR false;";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegionsWithOffset(odtFile, 0);
            Assertions.assertEquals(1, foldingDescriptors.length);
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];
            Assertions.assertEquals("...", foldingDescriptor.getPlaceholderText());
            Assertions.assertEquals("true OR false", foldingDescriptor.getElement().getText());
        });
    }

    @ParameterizedTest()
    @ValueSource(booleans = {true, false})
    void testDEFINECommandBlockIsCollapsedByDefault(boolean collapsedByDefault) {
        String content = "DEFINE COMMAND command => {\n" +
                "   @LOG('testing with a large block');\n" +
                "}";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegions(odtFile.getNode(), getEditor().getDocument());
            Assertions.assertEquals(1, foldingDescriptors.length);
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];

            final JavaCodeFoldingSettings settings = JavaCodeFoldingSettings.getInstance();
            settings.setCollapseMethods(collapsedByDefault);
            Assertions.assertEquals(collapsedByDefault, foldingBuilder.isCollapsedByDefault(foldingDescriptor.getElement()));
        });
    }

    @ParameterizedTest()
    @ValueSource(booleans = {true, false})
    void testCommandBlockIsNeverCollapsedByDefault(boolean collapsedByDefault) {
        String content = "IF true {\n" +
                "   @LOG('testing with a large block');\n" +
                "}";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegions(odtFile.getNode(), getEditor().getDocument());
            Assertions.assertEquals(1, foldingDescriptors.length);
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];

            final JavaCodeFoldingSettings settings = JavaCodeFoldingSettings.getInstance();
            settings.setCollapseMethods(collapsedByDefault);
            Assertions.assertFalse(foldingBuilder.isCollapsedByDefault(foldingDescriptor.getElement()));
        });
    }

    @ParameterizedTest()
    @ValueSource(booleans = {true, false})
    void testQueryIsCollapsedByDefault(boolean collapsedByDefault) {
        String content = "DEFINE QUERY query => true == false;";
        ODTFile odtFile = configureByText(content);
        ReadAction.run(() -> {
            FoldingDescriptor[] foldingDescriptors = foldingBuilder.buildFoldRegions(odtFile.getNode(), getEditor().getDocument());
            Assertions.assertEquals(1, foldingDescriptors.length);
            FoldingDescriptor foldingDescriptor = foldingDescriptors[0];

            final JavaCodeFoldingSettings settings = JavaCodeFoldingSettings.getInstance();
            settings.setCollapseMethods(collapsedByDefault);
            Assertions.assertEquals(collapsedByDefault, foldingBuilder.isCollapsedByDefault(foldingDescriptor.getElement()));
        });
    }
}
