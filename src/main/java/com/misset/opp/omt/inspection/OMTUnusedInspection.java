/*
 * Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */
package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import static com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL;

@ApiStatus.Internal
public class OMTUnusedInspection extends OMTMetaTypeInspectionBase {

    @Override
    protected @Nullable YamlMetaTypeProvider getMetaTypeProvider(@NotNull ProblemsHolder holder) {
        return OMTMetaTypeProvider.getInstance(holder.getProject());
    }

    @Override
    Logger getLogger() {
        return Logger.getInstance(OMTUnusedInspection.class);
    }

    @Override
    @NotNull
    protected PsiElementVisitor doBuildVisitor(@NotNull ProblemsHolder holder,
                                               @NotNull YamlMetaTypeProvider metaTypeProvider) {
        return new StructureChecker(holder, metaTypeProvider);
    }

    private static class StructureChecker extends SimpleYamlPsiVisitor {
        private final YamlMetaTypeProvider myMetaTypeProvider;
        private final ProblemsHolder myProblemsHolder;

        StructureChecker(@NotNull ProblemsHolder problemsHolder,
                         @NotNull YamlMetaTypeProvider metaTypeProvider) {
            myProblemsHolder = problemsHolder;
            myMetaTypeProvider = metaTypeProvider;
        }

        @Override
        public void visitElement(@NotNull PsiElement element) {
            if (element instanceof YAMLPlainTextImpl) {
                visitPlainTextValue((YAMLPlainTextImpl) element);
            } else {
                super.visitElement(element);
            }
        }

        private void visitPlainTextValue(@NotNull YAMLPlainTextImpl plainText) {
            YamlMetaTypeProvider.MetaTypeProxy meta = myMetaTypeProvider.getMetaTypeProxy(plainText);
            if (meta != null) {
                final YamlMetaType metaType = meta.getMetaType();
                if (metaType instanceof OMTNamedVariableMetaType) {
                    if (ReferencesSearch.search(plainText, new LocalSearchScope(plainText.getContainingFile()))
                            .findFirst() == null) {
                        String name = ((OMTNamedVariableMetaType) metaType).getName(plainText);
                        myProblemsHolder.registerProblem(
                                plainText,
                                name + " is never used",
                                LIKE_UNUSED_SYMBOL);
                    }
                }
            }
        }

        @Override
        protected void visitYAMLKeyValue(@NotNull YAMLKeyValue keyValue) {
            if (keyValue.getKey() == null) {
                return;
            }
            YamlMetaTypeProvider.MetaTypeProxy meta = myMetaTypeProvider.getKeyValueMetaType(keyValue);
            if (meta != null) {
                final YamlMetaType metaType = meta.getMetaType();
                if (metaType instanceof OMTModelItemMetaType) {
                    visitModelItem(meta, keyValue);
                }
            }
        }

        private void visitModelItem(YamlMetaTypeProvider.MetaTypeProxy meta,
                                    @NotNull YAMLKeyValue keyValue) {
            final YAMLValue value = keyValue.getValue();
            if (!(meta.getMetaType() instanceof OMTModelItemMetaType) || !(value instanceof YAMLMapping)) {
                return;
            }
            final boolean callable = ((OMTModelItemMetaType) meta.getMetaType()).isCallable((YAMLMapping) value);

            if (callable && ReferencesSearch.search(keyValue, keyValue.getUseScope()).findFirst() == null) {
                myProblemsHolder.registerProblem(
                        keyValue.getKey(),
                        keyValue.getKeyText() + " is never used",
                        LIKE_UNUSED_SYMBOL);
            }
        }

    }
}
