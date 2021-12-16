package com.misset.opp.omt.meta;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.misset.opp.omt.indexing.ExportedMembersIndex;
import com.misset.opp.omt.meta.arrays.OMTImportPathMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.settings.SettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;

import static com.misset.opp.util.ImportUtil.getOMTFile;

public class OMTImportMetaType extends OMTMetaMapType {
    private static final String MODULE = "module:";

    protected OMTImportMetaType() {
        super("OMT Import");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTImportPathMetaType();
    }

    public static HashMap<String, List<PsiElement>> getExportedMembersFromOMTFile(YAMLKeyValue keyValue) {
        return Optional.ofNullable(resolveToPath(keyValue))
                .map(path -> ExportedMembersIndex.getExportedMembers(path, keyValue.getProject()))
                .orElse(new HashMap<>());
    }

    public static OMTFile resolveToOMTFile(YAMLKeyValue keyValue) {
        return Optional.ofNullable(resolveToPath(keyValue))
                .map(path -> getOMTFile(path, keyValue.getProject()))
                .orElse(null);
    }

    public static String resolveToPath(YAMLKeyValue keyValue) {
        final SettingsState settingsState = SettingsState.getInstance(keyValue.getProject());
        final Collection<String> keySet = settingsState.mappingPaths.keySet();

        String path = keyValue.getKeyText();
        if (path.startsWith(MODULE)) {
            // TODO: resolve module from FS
            // String moduleName = path.substring(MODULE.length());
            return null;
        } else if (keySet.stream().anyMatch(path::startsWith)) {
            final Pair<String, String> mapEntry = keySet.stream().sorted(Comparator.reverseOrder())
                    .filter(path::startsWith)
                    .map(key -> new Pair<>(key, settingsState.mappingPaths.get(key)))
                    .findFirst()
                    .orElse(null);

            if (mapEntry == null) {
                return path;
            }
            String basePath = keyValue.getProject().getBasePath();
            String mapped = path.replace(mapEntry.getFirst(), mapEntry.getSecond());
            return String.format("%s/%s", basePath, mapped);
        } else {
            return Optional.ofNullable(keyValue.getContainingFile())
                    .map(PsiFile::getVirtualFile)
                    .map(VirtualFile::getParent)
                    .map(folder -> folder.findFileByRelativePath(path))
                    .map(OMTImportMetaType::getResolvablePath)
                    .orElse(null);
        }
    }

    private static String getResolvablePath(VirtualFile virtualFile) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return virtualFile.toString();
        }
        return virtualFile.getPath();
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
