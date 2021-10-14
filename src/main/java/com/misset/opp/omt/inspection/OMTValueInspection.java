package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.impl.YamlUnknownValuesInspectionBase;

public class OMTValueInspection extends YamlUnknownValuesInspectionBase {
    @Override
    protected @Nullable YamlMetaTypeProvider getMetaTypeProvider(@NotNull ProblemsHolder holder) {
        return new OMTMetaTypeProvider();
    }
}
