package com.misset.opp.omt.meta;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.indexing.ExportedMembersIndex;
import com.misset.opp.omt.meta.arrays.OMTImportPathMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.util.OMTImportUtil;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.misset.opp.omt.util.OMTImportUtil.getOMTFile;

public class OMTImportMetaType extends OMTMetaMapType {


    protected OMTImportMetaType() {
        super("OMT Import");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTImportPathMetaType();
    }

    public static HashMap<String, List<PsiCallable>> getExportedMembersFromOMTFile(YAMLKeyValue keyValue) {
        return Optional.ofNullable(resolveToPath(keyValue))
                .map(path -> OMTImportUtil.getOMTFile(path, keyValue.getProject()))
                .map(ExportedMembersIndex::getExportedMembers)
                .orElse(new HashMap<>());
    }

    public static OMTFile resolveToOMTFile(YAMLKeyValue keyValue) {
        return Optional.ofNullable(resolveToPath(keyValue))
                .map(path -> getOMTFile(path, keyValue.getProject()))
                .orElse(null);
    }
    public static String resolveToPath(YAMLKeyValue keyValue) {
        return OMTImportUtil.resolveToPath(keyValue.getProject(), (OMTFile) keyValue.getContainingFile(), keyValue.getKeyText());
    }

    @Override
    public void validateKey(@NotNull YAMLKeyValue keyValue,
                            @NotNull ProblemsHolder problemsHolder) {
        // the map with ImportPaths is not validated by the default validator
        if (keyValue.getValue() instanceof YAMLMapping) {
            final OMTImportPathMetaType omtImportPathMetaType = new OMTImportPathMetaType();
            ((YAMLMapping) keyValue.getValue()).getKeyValues()
                    .forEach(_keyValue -> omtImportPathMetaType.validateKey(_keyValue, problemsHolder));
        }
    }
}
