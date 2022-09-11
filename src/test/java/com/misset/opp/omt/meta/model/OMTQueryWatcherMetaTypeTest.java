package com.misset.opp.omt.meta.model;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.commands.LocalVariable;
import com.misset.opp.omt.testcase.OMTTestCase;
import com.misset.opp.resolvable.Resolvable;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTQueryWatcherMetaTypeTest extends OMTTestCase {

    @Test
    void getLocalVariablesFromOnChange() {
        configureByText(insideActivityWithPrefixes("watchers:\n" +
                "   - <caret>query: true\n" +
                "     onChange: |\n" +
                "           @LOG($newValue);\n"
        ));
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            YAMLMapping mapping = PsiTreeUtil.getParentOfType(elementAtCaret, YAMLMapping.class, false);
            List<LocalVariable> localVariables =
                    OMTQueryWatcherMetaType.getInstance().getLocalVariables(mapping);
            Assertions.assertEquals(2, localVariables.size());
            Assertions.assertTrue(localVariables.stream().allMatch(
                    Resolvable::isBoolean
            ));
        });
    }

}
