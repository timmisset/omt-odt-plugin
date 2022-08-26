package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.util.IncorrectOperationException;
import com.misset.opp.shared.refactoring.SupportsSafeDelete;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTYamlBindingParameterDelegate extends OMTYamlVariableDelegate implements SupportsSafeDelete {
    YAMLPlainTextImpl value;

    public OMTYamlBindingParameterDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue);
        this.value = yamlValue;
    }

    @Override
    public boolean isParameter() {
        return true;
    }

    @Override
    public void delete() throws IncorrectOperationException {

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
    public String getType() {
        return "binding parameter";
    }

    @Override
    public String getSource() {
        return "OMT binding";
    }
}
