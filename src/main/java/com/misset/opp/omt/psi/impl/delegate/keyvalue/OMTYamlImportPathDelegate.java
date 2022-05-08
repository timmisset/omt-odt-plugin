package com.misset.opp.omt.psi.impl.delegate.keyvalue;

import com.intellij.openapi.util.ModificationTracker;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.references.OMTImportPathReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

public class OMTYamlImportPathDelegate extends YAMLKeyValueImpl implements OMTYamlDelegate {

    private final YAMLKeyValue keyValue;

    private static long importsTrackerValue = 0;
    public static final ModificationTracker IMPORTS_TRACKER = () -> importsTrackerValue;

    public OMTYamlImportPathDelegate(YAMLKeyValue keyValue) {
        super(keyValue.getNode());
        this.keyValue = keyValue;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return OMTYamlDelegate.super.getReferences();
    }

    @Override
    public PsiReference getReference() {
        if (getKey() == null) {
            return null;
        }
        return new OMTImportPathReference(keyValue, getKey().getTextRangeInParent());
    }

    @Override
    public void subtreeChanged() {
        importsTrackerValue++;
    }
}
