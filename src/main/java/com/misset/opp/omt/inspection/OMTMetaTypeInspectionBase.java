package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeInspectionBase;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.function.Consumer;

public abstract class OMTMetaTypeInspectionBase extends YamlMetaTypeInspectionBase {
    @Override
    protected @Nullable YamlMetaTypeProvider getMetaTypeProvider(@NotNull ProblemsHolder holder) {
        return OMTMetaTypeProvider.getInstance(holder.getProject());
    }

    protected static <T extends OMTMetaType> void visitMetaTypeProxy(YAMLKeyValue keyValue,
                                                                     YamlMetaTypeProvider metaTypeProvider,
                                                                     Class<T> metaTypeClass,
                                                                     Consumer<T> typeConsumer) {
        if (keyValue.getKey() == null) {
            return;
        }
        YamlMetaTypeProvider.MetaTypeProxy meta = metaTypeProvider.getMetaTypeProxy(keyValue);
        if (meta != null) {
            final YamlMetaType metaType = meta.getMetaType();
            if (metaTypeClass.isAssignableFrom(metaType.getClass())) {
                typeConsumer.accept(metaTypeClass.cast(metaType));
            }
        }
    }
}
