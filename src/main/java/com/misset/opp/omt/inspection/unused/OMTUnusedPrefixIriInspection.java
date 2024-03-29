/*
 * Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */
package com.misset.opp.omt.inspection.unused;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.impl.delegate.keyvalue.OMTYamlPrefixIriDelegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

public class OMTUnusedPrefixIriInspection extends LocalInspectionTool {

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (!(element instanceof YAMLPsiElement) || !(element.getContainingFile() instanceof OMTFile)) {
                    return;
                }
                final PsiNamedElement delegate = OMTYamlDelegateFactory.createDelegate((YAMLPsiElement) element);
                if (delegate instanceof OMTYamlPrefixIriDelegate) {
                    final OMTYamlPrefixIriDelegate prefixIriDelegate = (OMTYamlPrefixIriDelegate) delegate;
                    if (prefixIriDelegate.getKey() != null && ((OMTYamlPrefixIriDelegate) delegate).isUnused()) {
                        holder.registerProblem(
                                prefixIriDelegate.getKey(),
                                prefixIriDelegate.getName() + " is never used",
                                LIKE_UNUSED_SYMBOL,
                                OMTRemoveQuickFix.getRemoveLocalQuickFix("prefix")
                        );
                    }
                }
            }
        };
    }
}
