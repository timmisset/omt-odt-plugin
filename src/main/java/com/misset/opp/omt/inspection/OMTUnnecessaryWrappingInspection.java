/*
 * Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */
package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.omt.meta.OMTImportMetaType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

@ApiStatus.Internal
public class OMTUnnecessaryWrappingInspection extends OMTMetaTypeInspectionBase {

    private static final Pattern WRAPPED_IMPORT = Pattern.compile("['\"]\\.[^'\"]+['\"]");
    protected static final String UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT = "Unnecessary wrapping of import statement";
    protected static final String UNWRAP_LOCAL_QUICKFIX_FAMILY_NAME = "Unwrap";


    @Override
    Logger getLogger() {
        return Logger.getInstance(OMTUnnecessaryWrappingInspection.class);
    }

    @Override
    @Nullable
    @Nls
    public String getStaticDescription() {
        return "Inspects coding style for unnecessary wrapping of import statements:<br>" +
                "imported paths using relative notation, ./someFile.omt:, are not required to be wrapped.<br>" +
                "only @client imports need to be wrapped";
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
        protected void visitYAMLKeyValue(@NotNull YAMLKeyValue keyValue) {
            visitMetaTypeProxy(keyValue, myMetaTypeProvider, OMTImportMetaType.class, (metaType) -> visitImport(keyValue));
        }

        private void visitImport(@NotNull YAMLKeyValue keyValue) {
            String importPath = Optional.of(keyValue).map(YAMLKeyValue::getKey).map(PsiElement::getText).orElse("");
            if (WRAPPED_IMPORT.matcher(importPath).find()) {
                myProblemsHolder.registerProblem(
                        keyValue.getKey(),
                        UNNECESSARY_WRAPPING_OF_IMPORT_STATEMENT,
                        getUnwrapQuickfix(element ->
                                YAMLElementGenerator
                                        .getInstance(element.getProject())
                                        .createYamlKeyValue(keyValue.getKeyText(), "Foo")
                                        .getKey())
                );
            }
        }

        private LocalQuickFix getUnwrapQuickfix(Function<PsiElement, PsiElement> replacement) {
            return new LocalQuickFix() {
                @Override
                public @IntentionFamilyName @NotNull String getFamilyName() {
                    return UNWRAP_LOCAL_QUICKFIX_FAMILY_NAME;
                }

                @Override
                public void applyFix(@NotNull Project project,
                                     @NotNull ProblemDescriptor descriptor) {
                    final PsiElement psiElement = descriptor.getPsiElement();
                    psiElement.replace(replacement.apply(psiElement));
                }
            };
        }

    }
}
