package com.misset.opp.omt.meta.actions;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

class OMTActionMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Set.of(OMTValueInspection.class, OMTMissingKeysInspection.class);
    }

    @Test
    void testGetVariableMap() {
        String content = insideActivityWithPrefixes("actions:\n" +
                "   myAction:\n" +
                "       params:\n" +
                "       - $paramA\n" +
                "       onSelect:\n" +
                "           @LOG($<caret>paramA);\n" +
                "");
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof YAMLPlainTextImpl);
        });
    }

    @Test
    void testDuplicateId() {
        configureByText("test.module.omt", "actions:\n" +
                "    notifications:\n" +
                "    - id: id\n" +
                "    - id: id\n");
        assertHasError("Duplicate");
    }

}
