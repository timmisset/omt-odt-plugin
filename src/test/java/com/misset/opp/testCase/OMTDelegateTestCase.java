package com.misset.opp.testCase;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import org.jetbrains.yaml.psi.YAMLPsiElement;

public class OMTDelegateTestCase extends OMTTestCase {

    protected OMTYamlDelegate getDelegateAtCaret() {
        return ReadAction.compute(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            if (elementAtCaret instanceof YAMLPsiElement) {
                return OMTYamlDelegateFactory.createDelegate((YAMLPsiElement) elementAtCaret);
            }
            return null;
        });
    }
}
