package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public abstract class OMTPlainTextReference extends PsiReferenceBase.Poly<YAMLPlainTextImpl> implements PsiPolyVariantReference {
    public OMTPlainTextReference(@NotNull YAMLPlainTextImpl element) {
        super(element, TextRange.allOf(element.getText()), false);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        final YAMLValue yamlValue = YAMLElementGenerator
                .getInstance(getElement().getProject())
                .createYamlKeyValue("dummy", newElementName)
                .getValue();
        if(yamlValue != null) { return myElement.replace(yamlValue); }
        return myElement;
    }

}
