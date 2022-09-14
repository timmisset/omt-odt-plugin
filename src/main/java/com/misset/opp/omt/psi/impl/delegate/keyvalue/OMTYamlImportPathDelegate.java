package com.misset.opp.omt.psi.impl.delegate.keyvalue;

import com.intellij.psi.PsiReference;
import com.misset.opp.omt.psi.references.OMTImportPathReference;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class OMTYamlImportPathDelegate extends OMTYamlKeyValueDelegateAbstract {

    private final transient YAMLKeyValue keyValue;

    public OMTYamlImportPathDelegate(YAMLKeyValue keyValue) {
        super(keyValue.getNode());
        this.keyValue = keyValue;
    }

    @Override
    public PsiReference getReference() {
        if (getKey() == null) {
            return null;
        }
        return new OMTImportPathReference(keyValue, getKey().getTextRangeInParent());
    }
}
