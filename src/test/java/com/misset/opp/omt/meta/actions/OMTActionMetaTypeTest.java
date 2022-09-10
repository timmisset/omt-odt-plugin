package com.misset.opp.omt.meta.actions;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.testcase.OMTTestCase;
import com.misset.opp.resolvable.local.LocalVariable;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

class OMTActionMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Set.of(OMTValueInspection.class, OMTMissingKeysInspection.class));
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
        inspection.assertHasError("Duplicate");
    }

    @Test
    void testNoValueVariableWhenOnSelectWithoutDynamicActionQuery() {
        String content = insideActivityWithPrefixes("actions:\n" +
                "   myAction:\n" +
                "       params:\n" +
                "       - $paramA\n" +
                "       onSelect<caret>:\n" +
                "           @LOG($value);\n" +
                "");
        configureByText(content);
        OMTMetaTypeProvider typeProvider = OMTMetaTypeProvider.getInstance(getProject());
        ReadAction.run(() -> {
            YAMLMapping actionMap = (YAMLMapping) typeProvider.getMetaOwner(myFixture.getElementAtCaret());
            OMTActionMetaType actionMetaType = (OMTActionMetaType) typeProvider.getResolvedMetaType(myFixture.getElementAtCaret());
            assertTrue(actionMetaType.getLocalVariables(actionMap).isEmpty());
        });
    }

    @Test
    void testValueVariableWhenOnSelectWithDynamicActionQuery() {
        String content = insideActivityWithPrefixes("actions:\n" +
                "   myAction:\n" +
                "       params:\n" +
                "       - $paramA\n" +
                "       dynamicActionQuery: /ont:someQuery\n" +
                "       onSelect<caret>:\n" +
                "           @LOG($value);\n" +
                "");
        configureByText(content);
        OMTMetaTypeProvider typeProvider = OMTMetaTypeProvider.getInstance(getProject());
        ReadAction.run(() -> {
            YAMLMapping actionMap = (YAMLMapping) typeProvider.getMetaOwner(myFixture.getElementAtCaret());
            OMTActionMetaType actionMetaType = (OMTActionMetaType) typeProvider.getResolvedMetaType(myFixture.getElementAtCaret());
            List<LocalVariable> localVariables = actionMetaType.getLocalVariables(actionMap);
            assertFalse(localVariables.isEmpty());
            assertTrue(localVariables.stream().anyMatch(localVariable -> localVariable.getName().equals("$value")));
        });
    }

}
