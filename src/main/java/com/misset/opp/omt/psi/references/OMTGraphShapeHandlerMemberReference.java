package com.misset.opp.omt.psi.references;

import com.intellij.psi.ResolveResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

public class OMTGraphShapeHandlerMemberReference extends OMTPlainTextReference {

    public OMTGraphShapeHandlerMemberReference(@NotNull YAMLPlainTextImpl element) {
        super(element);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return resolveExportableMemberReference();
    }
}
