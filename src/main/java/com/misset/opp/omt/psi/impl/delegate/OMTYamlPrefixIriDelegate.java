package com.misset.opp.omt.psi.impl.delegate;

import com.intellij.psi.PsiElement;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

public class OMTYamlPrefixIriDelegate extends YAMLKeyValueImpl implements OMTYamlDelegate {
    private final YAMLKeyValue keyValue;

    public OMTYamlPrefixIriDelegate(YAMLKeyValue keyValue) {
        super(keyValue.getNode());
        this.keyValue = keyValue;
    }

    public PsiElement getKey() {
        return keyValue.getKey();
    }

}
