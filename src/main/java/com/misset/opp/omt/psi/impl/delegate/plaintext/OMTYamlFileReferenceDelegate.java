package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.references.OMTFileReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTYamlFileReferenceDelegate extends YAMLPlainTextImpl implements OMTYamlDelegate {
    YAMLPlainTextImpl value;

    public OMTYamlFileReferenceDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue.getNode());
        this.value = yamlValue;
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NotNull String newName) {
        final YAMLKeyValue value = YAMLElementGenerator.getInstance(this.value.getProject())
                .createYamlKeyValue("foo", newName);
        return value.replace(value);
    }

    @Override
    public PsiReference getReference() {
        return new OMTFileReference(value);
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return OMTYamlDelegate.super.getReferences();
    }
}
