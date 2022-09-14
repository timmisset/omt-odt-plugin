package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.psi.references.OMTCallableReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTYamlPayloadQueryReferenceDelegate extends OMTYamlPlainTextDelegateAbstract implements PsiNamedElement {
    YAMLPlainTextImpl value;

    public OMTYamlPayloadQueryReferenceDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue.getNode());
        this.value = yamlValue;
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(String newName) {
        final YAMLKeyValue newValue = YAMLElementGenerator.getInstance(this.value.getProject())
                .createYamlKeyValue("foo", newName);
        return value.replace(newValue);
    }

    @Override
    public PsiReference getReference() {
        return new OMTCallableReference(value);
    }
}
