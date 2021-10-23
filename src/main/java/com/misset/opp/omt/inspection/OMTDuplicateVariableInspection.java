package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeInspectionBase;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;

public class OMTDuplicateVariableInspection extends YamlMetaTypeInspectionBase {

    @Override
    protected @Nullable YamlMetaTypeProvider getMetaTypeProvider(@NotNull ProblemsHolder holder) {
        return OMTMetaTypeProvider.getInstance(holder.getProject());
    }

    @Override
    protected @NotNull PsiElementVisitor doBuildVisitor(@NotNull ProblemsHolder holder,
                                                        @NotNull YamlMetaTypeProvider metaTypeProvider) {
        return new StructureChecker(holder, metaTypeProvider);
    }

    protected class StructureChecker extends SimpleYamlPsiVisitor {
        private final YamlMetaTypeProvider yamlMetaTypeProvider;
        private final ProblemsHolder problemsHolder;

        public StructureChecker(@NotNull ProblemsHolder problemsHolder,
                                @NotNull YamlMetaTypeProvider yamlMetaTypeProvider) {
            this.problemsHolder = problemsHolder;
            this.yamlMetaTypeProvider = yamlMetaTypeProvider;
        }

        @Override
        protected void visitYAMLMapping(@NotNull YAMLMapping mapping) {
            final YamlMetaTypeProvider.MetaTypeProxy meta = yamlMetaTypeProvider.getMetaTypeProxy(mapping);
            if (meta == null) {
                return;
            }

            final YamlMetaType metaType = meta.getMetaType();
            if (metaType instanceof OMTVariableProvider) {
                final OMTVariableProvider variableProvider = (OMTVariableProvider) metaType;
                inspectMap(variableProvider.getVariableMap(mapping));
            }
        }

        private void inspectMap(HashMap<String, List<PsiElement>> variableMap) {
            variableMap.values()
                    .stream()
                    .filter(psiElements -> psiElements.size() > 1)
                    .forEach(psiElements -> psiElements.forEach(psiElement -> problemsHolder.registerProblem(
                            psiElement,
                            "Duplication")));
        }
    }
}
