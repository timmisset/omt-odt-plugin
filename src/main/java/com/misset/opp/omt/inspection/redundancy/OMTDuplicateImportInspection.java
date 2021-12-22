package com.misset.opp.omt.inspection.redundancy;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.omt.inspection.OMTMetaTypeInspectionBase;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTDuplicateImportInspection extends OMTMetaTypeInspectionBase {

    @Override
    protected @Nullable YamlMetaTypeProvider getMetaTypeProvider(@NotNull ProblemsHolder holder) {
        return OMTMetaTypeProvider.getInstance(holder.getProject());
    }

    @Override
    protected @NotNull PsiElementVisitor doBuildVisitor(@NotNull ProblemsHolder holder,
                                                        @NotNull YamlMetaTypeProvider metaTypeProvider) {
        return new StructureChecker(holder, metaTypeProvider);
    }

    protected static class StructureChecker extends SimpleYamlPsiVisitor {
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
            if (metaType instanceof OMTImportMetaType) {
                inspectMap(mapping);
            }
        }

        private void inspectMap(YAMLMapping mapping) {
            // inspect the map for duplicate import paths (with possible different access methods)
            // group keys by resolving to their absolute path
            mapping.getKeyValues().stream()
                    .collect(Collectors.groupingBy(this::nullSafePathMap))
                    .forEach(this::inspectInstances);

            // then inspect all the import members within a single key-value
            mapping.getKeyValues().forEach(this::inspectImportMembers);
        }

        private String nullSafePathMap(YAMLKeyValue keyValue) {
            return Optional.ofNullable(OMTImportMetaType.resolveToPath(keyValue))
                    .orElse("");
        }

        private void inspectImportMembers(YAMLKeyValue keyValue) {
            YAMLValue value = keyValue.getValue();
            if (value instanceof YAMLSequence) {
                ((YAMLSequence) value).getItems().stream()
                        .collect(Collectors.groupingBy(this::getMemberName))
                        .forEach(this::inspectInstances);
            }
        }

        private String getMemberName(YAMLSequenceItem sequenceItem) {
            return Optional.ofNullable(sequenceItem.getValue())
                    .map(PsiElement::getText)
                    .orElse("");
        }

        private void inspectInstances(String name, List<? extends YAMLPsiElement> instances) {
            if (instances.size() > 1) {
                instances.forEach(this::registerProblem);
            }
        }

        private void registerProblem(PsiElement psiElement) {
            if (psiElement instanceof YAMLKeyValue) {
                psiElement = ((YAMLKeyValue) psiElement).getKey();
            }
            if (psiElement == null) {
                return;
            }
            problemsHolder.registerProblem(
                    psiElement,
                    "Duplication");
        }
    }
}
