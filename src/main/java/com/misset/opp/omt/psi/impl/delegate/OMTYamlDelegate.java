package com.misset.opp.omt.psi.impl.delegate;

import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Delegates provide functionalities for specific implementations of YAML elements KeyValue and PlainText.
 * The delegate is stored as UserData using the OMTYamlDelegateFactory
 */
public interface OMTYamlDelegate extends PsiNamedElement {

    @Override
    default PsiReference @NotNull [] getReferences() {
        // override the getReferences is required because the YAML base class expects to
        // get references provided via the Reference
        return Optional.ofNullable(getReference())
                .stream()
                .toArray(PsiReference[]::new);
    }

    void subtreeChanged();

}
