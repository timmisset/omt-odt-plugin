package com.misset.opp.omt.psi.impl.delegate.keyvalue;

import com.intellij.psi.PsiReference;
import com.misset.opp.omt.psi.references.OMTDeclaredInterfaceReference;
import org.jetbrains.yaml.psi.YAMLKeyValue;

public class OMTYamlDeclaredInterfaceDelegate extends OMTYamlKeyValueDelegateAbstract {

    private final transient YAMLKeyValue keyValue;

    public OMTYamlDeclaredInterfaceDelegate(YAMLKeyValue keyValue) {
        super(keyValue.getNode());
        this.keyValue = keyValue;
    }

    @Override
    public PsiReference getReference() {
        if (getKey() == null) {
            return null;
        }
        return new OMTDeclaredInterfaceReference(keyValue, getKey().getTextRangeInParent());
    }
}
