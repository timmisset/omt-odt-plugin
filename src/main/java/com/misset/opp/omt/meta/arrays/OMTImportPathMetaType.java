package com.misset.opp.omt.meta.arrays;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import com.misset.opp.omt.meta.OMTImportMetaType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.nio.file.Path;
import java.util.Optional;

public class OMTImportPathMetaType extends OMTSortedArrayMetaType {
    public OMTImportPathMetaType() {
        super(new OMTImportMemberMetaType());
    }


    @Override
    public void validateKey(@NotNull YAMLKeyValue keyValue,
                            @NotNull ProblemsHolder problemsHolder) {
        if (keyValue.getKey() == null) {
            return;
        }
        // validate that the key can be resolved to file:
        // resolve to the virtual file instead of the Psi to prevent the file from being loaded without cause
        final boolean resolvable = Optional.ofNullable(OMTImportMetaType.resolveToPath(keyValue))
                .filter(s -> !s.startsWith("temp:///")) // <-- unittests use a fictitious path that is not valid for Path
                .map(Path::of)
                .map(VirtualFileManager.getInstance()::findFileByNioPath)
                .map(VirtualFile::exists)
                .orElse(false);
        if (!resolvable) {
            problemsHolder.registerProblem(keyValue.getKey(), "Cannot find file", ProblemHighlightType.ERROR);
        }

        // validate the sequence itself, check if it's ordered:
        Optional.ofNullable(keyValue.getValue())
                .ifPresent(value -> new OMTImportPathMetaType().validateValue(value, problemsHolder));
    }
}
