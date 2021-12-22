package com.misset.opp.omt.psi.impl.delegate;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.psi.references.OMTParamTypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.regex.Matcher;

import static com.misset.opp.omt.meta.scalars.OMTParamTypeType.CURIE_PATTERN;

public class OMTYamlParamTypeDelegate extends YAMLPlainTextImpl implements OMTYamlDelegate {
    YAMLPlainTextImpl value;

    public OMTYamlParamTypeDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue.getNode());
        this.value = yamlValue;
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(String newName) {
        final YAMLKeyValue value = YAMLElementGenerator.getInstance(this.value.getProject())
                .createYamlKeyValue("foo", newName);
        return replace(value);
    }

    @Override
    public OMTParamTypeReference getReference() {
        Matcher matcher = CURIE_PATTERN.matcher(value.getTextValue());
        boolean b = matcher.find();
        if (!b) {
            return null;
        }

        return new OMTParamTypeReference(value, TextRange.create(matcher.start(1), matcher.end(1)));
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return OMTYamlDelegate.super.getReferences();
    }
}
