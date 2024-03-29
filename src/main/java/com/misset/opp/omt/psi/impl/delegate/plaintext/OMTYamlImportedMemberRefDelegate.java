package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.psi.references.OMTImportedMemberRefReference;
import com.misset.opp.omt.util.OMTRefactoringUtil;
import com.misset.opp.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTYamlImportedMemberRefDelegate extends OMTYamlPlainTextDelegateAbstract implements PsiNamedElement,
        SupportsSafeDelete {
    YAMLPlainTextImpl value;

    public OMTYamlImportedMemberRefDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue.getNode());
        this.value = yamlValue;
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NotNull String newName) {
        final YAMLKeyValue newValue = YAMLElementGenerator.getInstance(this.value.getProject())
                .createYamlKeyValue("foo", newName);
        return value.replace(newValue);
    }

    @Override
    public PsiReference getReference() {
        return new OMTImportedMemberRefReference(value);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        OMTRefactoringUtil.removeFromSequence(this);
    }

    @Override
    public boolean isUnused() {
        return false;
    }
}
