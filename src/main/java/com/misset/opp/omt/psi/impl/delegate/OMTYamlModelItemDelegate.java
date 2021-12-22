package com.misset.opp.omt.psi.impl.delegate;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

public class OMTYamlModelItemDelegate extends YAMLKeyValueImpl implements OMTYamlDelegate {

    private final YAMLKeyValue keyValue;
    private final YAMLMapping mapping;
    private final OMTModelItemMetaType metaType = new OMTModelItemMetaType("modelItem");

    public OMTYamlModelItemDelegate(YAMLKeyValue keyValue) {
        super(keyValue.getNode());
        this.keyValue = keyValue;
        this.mapping = (YAMLMapping) keyValue.getValue();
    }

    public boolean isCallable() {
        return metaType.isCallable(mapping);
    }

    public String getName() {
        return keyValue.getName();
    }

    public PsiElement getKey() {
        return keyValue.getKey();
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return OMTYamlDelegate.super.getReferences();
    }
}
