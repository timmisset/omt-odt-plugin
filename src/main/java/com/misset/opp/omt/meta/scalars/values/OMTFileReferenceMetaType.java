package com.misset.opp.omt.meta.scalars.values;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FilenameIndex;
import com.misset.opp.omt.completion.OMTCompletions;
import com.misset.opp.util.ImportUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.CompletionContext;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.YAMLValue;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class OMTFileReferenceMetaType extends YamlStringType {
    private final String extension;

    protected OMTFileReferenceMetaType(String extension) {
        super();
        this.extension = extension;
    }

    @Override
    public @NotNull List<? extends LookupElement> getValueLookups(@NotNull YAMLScalar insertedScalar, @Nullable CompletionContext completionContext) {
        // the path of the current file, needs to be the original file not the temp completion file
        VirtualFile fromFile = Optional.ofNullable(OMTCompletions.getPlaceholderToken())
                .map(OMTCompletions::getCompletionParameters)
                .map(CompletionParameters::getOriginalFile)
                .map(PsiFile::getVirtualFile)
                .orElse(null);
        if (fromFile == null) {
            return Collections.emptyList();
        }

        String basePath = insertedScalar.getProject().getBasePath();
        return FilenameIndex.getAllFilesByExt(insertedScalar.getProject(), extension)
                .stream()
                .map(virtualFile -> {
                    String pathToFile = ImportUtil.getPathToFile(fromFile, virtualFile);
                    Path folder = Path.of(virtualFile.getPath()).getParent();
                    String presentableFolderName = folder.toString().replace("\\", "/").replace(basePath == null ? "" : basePath, "");
                    return LookupElementBuilder.create(pathToFile)
                            .withLookupString(virtualFile.getName())
                            .withLookupString(virtualFile.getNameWithoutExtension())
                            .withPresentableText(virtualFile.getName())
                            .withTypeText(presentableFolderName);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void validateValue(@NotNull YAMLValue value, @NotNull ProblemsHolder problemsHolder) {
        if (!value.getText().endsWith(".json")) {
            problemsHolder.registerProblem(value, ".json file required", ProblemHighlightType.ERROR);
        }
        PsiReference reference = value.getReference();
        if (reference != null && reference.resolve() == null) {
            problemsHolder.registerProblem(value, "Cannot find file", ProblemHighlightType.ERROR);
        }
    }
}
