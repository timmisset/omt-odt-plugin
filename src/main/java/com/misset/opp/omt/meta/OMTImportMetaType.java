package com.misset.opp.omt.meta;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiFile;
import com.misset.opp.omt.meta.arrays.OMTImportPathMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.util.OMTImportUtil;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.misset.opp.omt.util.OMTImportUtil.getOMTFile;

public class OMTImportMetaType extends OMTMetaMapType {

    private static final Logger LOGGER = Logger.getInstance(OMTImportMetaType.class);
    private static final OMTImportMetaType INSTANCE = new OMTImportMetaType();

    public static OMTImportMetaType getInstance() {
        return INSTANCE;
    }

    private OMTImportMetaType() {
        super("OMT Import");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return OMTImportPathMetaType.getInstance();
    }

    public Map<String, Collection<PsiCallable>> getExportedMembersFromOMTFile(YAMLKeyValue keyValue) {
        if (!keyValue.isValid()) {
            return new HashMap<>();
        }
        return Optional.ofNullable(resolveToPath(keyValue))
                .map(path -> OMTImportUtil.getOMTFile(path, keyValue.getProject()))
                .map(OMTFile::getExportingMembersMap)
                .orElse(new HashMap<>());
    }

    public OMTFile resolveToOMTFile(YAMLKeyValue keyValue) {
        if (!keyValue.isValid()) {
            return null;
        }
        return LoggerUtil.computeWithLogger(LOGGER, "Resolving path " + keyValue.getKeyText(),
                () -> Optional.ofNullable(resolveToPath(keyValue))
                        .map(path -> getOMTFile(path, keyValue.getProject()))
                        .orElse(null));
    }

    public String resolveToPath(YAMLKeyValue keyValue) {
        PsiFile containingFile = keyValue.getContainingFile();
        if (!(containingFile instanceof OMTFile)) {
            return null;
        }
        return OMTImportUtil.resolveToPath(keyValue.getProject(), (OMTFile) containingFile, keyValue.getKeyText());
    }

    @Override
    public void validateKey(@NotNull YAMLKeyValue keyValue,
                            @NotNull ProblemsHolder problemsHolder) {
        // the map with ImportPaths is not validated by the default validator
        if (keyValue.getValue() instanceof YAMLMapping) {
            final OMTImportPathMetaType omtImportPathMetaType = OMTImportPathMetaType.getInstance();
            ((YAMLMapping) keyValue.getValue()).getKeyValues()
                    .forEach(yamlKeyValue -> omtImportPathMetaType.validateKey(yamlKeyValue, problemsHolder));
        }
    }
}
