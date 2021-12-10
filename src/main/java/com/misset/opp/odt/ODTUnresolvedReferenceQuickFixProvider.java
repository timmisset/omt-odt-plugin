package com.misset.opp.odt;

import com.intellij.codeInsight.daemon.QuickFixActionRegistrar;
import com.intellij.codeInsight.quickfix.UnresolvedReferenceQuickFixProvider;
import com.misset.opp.odt.psi.reference.ODTCallReference;
import org.jetbrains.annotations.NotNull;

public class ODTUnresolvedReferenceQuickFixProvider extends UnresolvedReferenceQuickFixProvider<ODTCallReference> {

    @Override
    public void registerFixes(@NotNull ODTCallReference ref, @NotNull QuickFixActionRegistrar registrar) {
        System.out.println("here");
    }

    @Override
    public @NotNull Class<ODTCallReference> getReferenceClass() {
        return ODTCallReference.class;
    }


}
