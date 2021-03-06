package com.misset.opp.omt.inspection.structure;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.impl.YamlUnknownKeysInspectionBase;

public class OMTUnkownKeysInspection extends YamlUnknownKeysInspectionBase {
    @Override
    protected @Nullable YamlMetaTypeProvider getMetaTypeProvider(@NotNull ProblemsHolder holder) {
        return OMTMetaTypeProvider.getInstance(holder.getProject());
    }
}
