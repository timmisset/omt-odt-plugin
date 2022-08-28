package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTYamlBaseParameterDelegate extends OMTYamlVariableDelegate implements SupportsSafeDelete {
    YAMLPlainTextImpl value;

    public OMTYamlBaseParameterDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue);
        this.value = yamlValue;
    }

    @Override
    public boolean isParameter() {
        return true;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(value, YAMLKeyValue.class);
        if (keyValue != null) {
            keyValue.delete();
        }
    }

    @Override
    public String getType() {
        return "base parameter";
    }

    @Override
    public String getSource() {
        return "OMT parameter";
    }

}
