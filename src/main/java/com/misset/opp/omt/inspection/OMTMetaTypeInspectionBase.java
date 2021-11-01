package com.misset.opp.omt.inspection;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeInspectionBase;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.time.Duration;
import java.time.LocalDateTime;
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


    private LocalDateTime dateTime;
    @Override
    public void inspectionStarted(@NotNull LocalInspectionToolSession session,
                                  boolean isOnTheFly) {
        dateTime = LocalDateTime.now();
    }

    @Override
    public void inspectionFinished(@NotNull LocalInspectionToolSession session,
                                   @NotNull ProblemsHolder problemsHolder) {
        getLogger().warn("Inspection took: " + Duration.between(dateTime, LocalDateTime.now()).toMillis() + " ms");
    }

    abstract Logger getLogger();
}
