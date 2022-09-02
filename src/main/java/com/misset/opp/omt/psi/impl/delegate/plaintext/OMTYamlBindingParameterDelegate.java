package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.util.IncorrectOperationException;
import com.misset.opp.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

@SuppressWarnings("java:S110")
public class OMTYamlBindingParameterDelegate extends OMTYamlVariableDelegate implements SupportsSafeDelete {
    YAMLPlainTextImpl yamlPlainText;

    public OMTYamlBindingParameterDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue);
        this.yamlPlainText = yamlValue;
    }

    @Override
    public boolean isParameter() {
        return true;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        // don't remove
    }

    /**
     * A binding parameter is linked to the Typescript layer and thus should be removed by the user
     * There is currently no method to safe-delete these parameters since it would require
     * removing the parameter definition from the Typescript component and all HTML implementations
     * of that component
     */
    @Override
    public boolean isUnused() {
        return false;
    }

    @Override
    public String getSource() {
        return "OMT binding";
    }

}
