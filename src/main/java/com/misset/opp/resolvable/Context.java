package com.misset.opp.resolvable;

import com.intellij.psi.PsiFile;
import com.misset.opp.resolvable.psi.PsiCall;

import java.util.Set;

public interface Context {

    /**
     * The call that is going to be resolved
     */
    PsiCall getCall();

    /**
     * Calls can be made to external files, populate this set so the CachedValueManager
     * can very narrowly determine if it should recalculate the outcome
     */
    Set<PsiFile> getFilesInScope();
}
