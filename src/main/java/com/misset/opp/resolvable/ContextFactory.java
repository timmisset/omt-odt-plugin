package com.misset.opp.resolvable;

import com.intellij.psi.PsiFile;
import com.misset.opp.resolvable.psi.PsiCall;

import java.util.HashSet;
import java.util.Set;

public class ContextFactory {
    private ContextFactory() {
        // empty constructor
    }

    public static Context fromCall(PsiCall call) {
        return new Context() {
            private final Set<PsiFile> files = new HashSet<>();
            private final PsiCall myCall = call;

            @Override
            public PsiCall getCall() {
                return myCall;
            }

            @Override
            public Set<PsiFile> getFilesInScope() {
                return files;
            }
        };
    }

}
