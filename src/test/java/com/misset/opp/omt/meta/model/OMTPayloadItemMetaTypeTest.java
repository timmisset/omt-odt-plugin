package com.misset.opp.omt.meta.model;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.inspection.structure.OMTMissingKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.local.LocalVariable;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Set;

class OMTPayloadItemMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Set.of(OMTUnkownKeysInspection.class, OMTMissingKeysInspection.class, OMTValueInspection.class);
    }

    @Test
    void testHasErrorWhenNoValueOrQuery() {
        configureByText(insideActivityWithPrefixes("payload:\n" +
                "   payloadItem:\n" +
                "       list: true"));
        assertHasError("Missing required key(s): 'value'");
    }

    @Test
    void testHasNoErrorWhenValueIsPresent() {
        configureByText(insideActivityWithPrefixes("payload:\n" +
                "   payloadItem:\n" +
                "       value: true\n" +
                "       list: true"));
        assertNoError("Missing required key(s): 'value'");
    }

    @Test
    void testHasNoErrorWhenQueryIsPresent() {
        configureByText(insideActivityWithPrefixes("payload:\n" +
                "   payloadItem:\n" +
                "       query: true\n" +
                "       list: true"));
        assertNoError("Missing required key(s): 'value'");
    }

    @Test
    void testHasErrorWhenBothValueAndQueryArePresent() {
        configureByText(insideActivityWithPrefixes("payload:\n" +
                "   payloadItem:\n" +
                "       query: true\n" +
                "       value: true\n" +
                "       list: true"));
        assertHasError("Use either 'value' or 'query'");
    }

    @Test
    void testHasErrorWhenCombiningQueryAndOnChange() {
        configureByText(insideActivityWithPrefixes("payload:\n" +
                "   payloadItem:\n" +
                "       query: true\n" +
                "       onChange: |\n" +
                "           @LOG($newValue);\n" +
                "       list: true"));
        assertHasError("Cannot use 'onChange' with payload query");
    }

    @Test
    void testHasNoErrorWhenCombiningValueAndOnChange() {
        configureByText(insideActivityWithPrefixes("payload:\n" +
                "   payloadItem:\n" +
                "       value: true\n" +
                "       onChange: |\n" +
                "           @LOG($newValue);\n" +
                "       list: true"));
        assertNoError("Cannot use 'onChange' with payload query");
    }

    @Test
    void getLocalVariablesFromOnChange() {
        configureByText(insideActivityWithPrefixes("payload:\n" +
                "   payloadItem:\n" +
                "       <caret>value: true\n" +
                "       onChange: |\n" +
                "           @LOG($newValue);\n" +
                "       list: true"));
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            YAMLMapping mapping = PsiTreeUtil.getParentOfType(elementAtCaret, YAMLMapping.class, false);
            List<LocalVariable> localVariables =
                    OMTPayloadItemMetaType.getInstance().getLocalVariables(mapping);
            Assertions.assertEquals(2, localVariables.size());
            Assertions.assertTrue(localVariables.stream().allMatch(
                    Resolvable::isBoolean
            ));
        });
    }
}
