package com.misset.opp.omt.inspection.structure;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.impl.YamlMissingKeysInspectionBase;

public class OMTMissingKeysInspection extends YamlMissingKeysInspectionBase {
    @Override
    protected @Nullable YamlMetaTypeProvider getMetaTypeProvider(@NotNull ProblemsHolder holder) {
        return OMTMetaTypeProvider.getInstance(holder.getProject());
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly,
                                                   @NotNull LocalInspectionToolSession session) {
        if (holder.getFile() instanceof OMTFile) {
            return super.buildVisitor(holder, isOnTheFly, session);
        } else {
            return new PsiElementVisitor() {
            };
        }
    }
}
