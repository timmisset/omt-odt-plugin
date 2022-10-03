package com.misset.opp.odt.psi;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import com.misset.opp.odt.testcase.ODTFileTestImpl;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Collection;
import java.util.List;

class PsiRelationshipUtilTest extends ODTTestCase {

    @ParameterizedTest
    @ValueSource(strings = {
            "VAR $variable = true; @LOG($variable);",
            "VAR $variable = true; IF true { @LOG($variable); }"
    })
    void testCanBeRelatedElementTrue(String content) {
        assertCanBeRelatedElement(content, true);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "@LOG($variable); VAR $variable = true;",
    })
    void testCanBeRelatedElementFalse(String content) {
        assertCanBeRelatedElement(content, false);
    }

    private void assertCanBeRelatedElement(String content, boolean canBeRelated) {
        ODTFileTestImpl odtFileTest = configureByText(content);
        ReadAction.run(() -> {
            Collection<ODTVariable> childrenOfType = PsiTreeUtil.findChildrenOfType(odtFileTest, ODTVariable.class);
            ODTVariable declaringVariable = childrenOfType.stream().filter(ODTVariableDelegate::isDeclaredVariable).findFirst().orElse(null);
            ODTVariable usageVariable = childrenOfType.stream().filter(variable -> !variable.isDeclaredVariable()).findFirst().orElse(null);

            assertEquals(canBeRelated, PsiRelationshipUtil.canBeRelatedElement(declaringVariable, usageVariable));
        });
    }

    @Test
    void getRelatedElementsReturnsAllRelatedElements() {
        String content = "" +
                "VAR $variable = 'test';\n" +
                "$variable = 'new value';\n" +
                "IF true { $variable = 'not included'; } ELSE {\n" +
                "   $variable = 'included';\n" +
                "   @LOG(<caret>$variable);\n" +
                "}\n";
        ODTFileTestImpl file = configureByText(content);

        underProgress(() -> ReadAction.run(() -> {
            int offset = getEditor().getCaretModel().getOffset();
            PsiElement variable = file.findElementAt(offset).getParent();
            assertTrue(variable instanceof ODTVariable);
            List<PsiElement> relatedElements = PsiRelationshipUtil.getRelatedElements(variable);
            assertEquals(3, relatedElements.size());
            assertEquals("$variable = 'included'", relatedElements.get(0).getParent().getText());
            assertEquals("$variable = 'new value'", relatedElements.get(1).getParent().getText());
            assertEquals("$variable = 'test'", relatedElements.get(2).getParent().getText());
        }));
    }
}
