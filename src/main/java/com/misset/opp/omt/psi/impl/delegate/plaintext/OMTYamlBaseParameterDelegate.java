package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

@SuppressWarnings("java:S110")
public class OMTYamlBaseParameterDelegate extends OMTYamlVariableDelegate implements SupportsSafeDelete {
    YAMLPlainTextImpl yamlPlainText;

    public OMTYamlBaseParameterDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue);
        this.yamlPlainText = yamlValue;
    }

    @Override
    public boolean isParameter() {
        return true;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(yamlPlainText, YAMLKeyValue.class);
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
