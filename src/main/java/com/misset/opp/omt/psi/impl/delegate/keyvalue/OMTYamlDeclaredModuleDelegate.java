package com.misset.opp.omt.psi.impl.delegate.keyvalue;

import com.intellij.psi.PsiReference;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.references.OMTDeclaredModuleReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

public class OMTYamlDeclaredModuleDelegate extends YAMLKeyValueImpl implements OMTYamlDelegate {

    private final YAMLKeyValue keyValue;

    public OMTYamlDeclaredModuleDelegate(YAMLKeyValue keyValue) {
        super(keyValue.getNode());
        this.keyValue = keyValue;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return OMTYamlDelegate.super.getReferences();
    }

    @Override
    public PsiReference getReference() {
        if (getKey() == null) {
            return null;
        }
        return new OMTDeclaredModuleReference(keyValue, getKey().getTextRangeInParent());
    }

}
