package com.misset.opp.omt.psi.references;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Optional;

public abstract class OMTPlainTextReference extends PsiReferenceBase.Poly<YAMLPlainTextImpl> implements PsiPolyVariantReference {
    protected TextRange textRange;

    public OMTPlainTextReference(@NotNull YAMLPlainTextImpl element) {
        this(element, TextRange.allOf(element.getText()));
    }

    public OMTPlainTextReference(@NotNull YAMLPlainTextImpl element,
                                 TextRange textRange) {
        super(element, textRange, false);
        this.textRange = textRange;
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return handleElementRename(newElementName, textRange);
    }

    protected PsiElement handleElementRename(@NotNull String newElementName,
                                             TextRange textRange) {
        return Optional.ofNullable(YAMLElementGenerator.getInstance(myElement.getProject()))
                .map(generator -> generator.createYamlKeyValue("foo",
                        textRange.replace(myElement.getText(), newElementName)))
                .map(YAMLKeyValue::getValue)
                .map(myElement::replace)
                .orElse(myElement);
    }

}
