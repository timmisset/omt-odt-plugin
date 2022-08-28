package com.misset.opp.omt.inspection.redundancy;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.omt.inspection.OMTMetaTypeInspectionBase;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collection;
import java.util.Map;

public class OMTDuplicateVariableInspection extends OMTMetaTypeInspectionBase {

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
            if (!(mapping.getContainingFile() instanceof OMTFile)) {
                return;
            }
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

        private void inspectMap(Map<String, Collection<PsiVariable>> variableMap) {
            variableMap.values()
                    .stream()
                    .filter(psiElements -> psiElements.size() > 1)
                    .forEach(psiElements -> psiElements.forEach(this::registerProblem));
        }

        private void registerProblem(PsiElement psiElement) {
            if (psiElement == null) {
                return;
            }
            problemsHolder.registerProblem(psiElement.getOriginalElement(), "Duplication");
        }
    }
}
