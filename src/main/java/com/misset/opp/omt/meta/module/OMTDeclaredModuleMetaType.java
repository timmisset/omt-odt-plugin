package com.misset.opp.omt.meta.module;

import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.completion.OMTCompletions;
import com.misset.opp.omt.meta.OMTMetaMapType;
import com.misset.opp.omt.util.OMTModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTDeclaredModuleMetaType extends OMTMetaMapType {
    private static final OMTDeclaredModuleMetaType INSTANCE = new OMTDeclaredModuleMetaType();

    public static OMTDeclaredModuleMetaType getInstance() {
        return INSTANCE;
    }

    private OMTDeclaredModuleMetaType() {
        super("module");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTDeclaredInterfaceMetaType.getInstance();
    }

    @Override
    public @NotNull List<Field> computeKeyCompletions(@Nullable YAMLMapping existingMapping) {
        YAMLKeyValue keyValue = PsiTreeUtil.getParentOfType(OMTCompletions.getPlaceholderToken(), YAMLKeyValue.class);
        if (keyValue == null) {
            return Collections.emptyList();
        }
        return Optional.ofNullable(OMTModuleUtil.getModule(keyValue.getProject(), keyValue.getKeyText()))
                .map(OMTModuleUtil::getExportedMemberNames)
                .stream()
                .flatMap(Collection::stream)
                .map(s -> new Field(s, OMTDeclaredModuleMetaType.getInstance()))
                .collect(Collectors.toList());
    }
}
