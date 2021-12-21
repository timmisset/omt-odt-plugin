package com.misset.opp.omt.meta.module;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.misset.opp.omt.completion.OMTCompletions;
import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class OMTDeclareMetaType extends OMTMetaMapType {
    public OMTDeclareMetaType() {
        super("Declare");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTDeclaredModuleMetaType(name);
    }

    @Override
    public @NotNull List<Field> computeKeyCompletions(@Nullable YAMLMapping existingMapping) {
        PsiElement element = OMTCompletions.getPlaceholderToken();
        CompletionParameters completionParameters = OMTCompletions.getCompletionParameters(element);
        if (element == null || DumbService.isDumb(element.getProject())) {
            return Collections.emptyList();
        }

        Project project = element.getProject();
        PsiManager manager = PsiManager.getInstance(project);
        return FilenameIndex.getAllFilesByExt(project, "module.omt")
                .stream()
                .map(manager::findFile)
                .filter(psiFile -> psiFile != null && psiFile != completionParameters.getOriginalFile())
                .map(OMTFile.class::cast)
                .map(OMTFile::getModuleName)
                .map(s -> new Field(s, new OMTDeclaredModuleMetaType(s)))
                .collect(Collectors.toList());
    }
}
