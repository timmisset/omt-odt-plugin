package com.misset.opp.omt.util;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.omt.indexing.ExportedMembersIndex;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.settings.SettingsState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.*;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;

public class OMTImportUtil {
    private static final String MODULE = "module:";

    public static IntentionAction[] getImportIntentions(OMTFile file, PsiCall call) {
        return ExportedMembersIndex.getExportedMemberLocationsByName(call.getCallId())
                .stream()
                .map(psiCallable -> getIntentionActions(file, psiCallable))
                .toArray(IntentionAction[]::new);
    }

    /**
     * Returns the preferred method to import the PsiCallable element into the OMTFile
     * If the file and call are part of the same module, it will return a relative path (./subfolder/someFile.omt)
     * If the file and call are part of different modules, it will return shorthand import (@domain/...)
     */
    public static String getImportPath(OMTFile file, PsiCallable callable) {
        OMTFile importedFile = getImportedFile(callable);
        if (importedFile == null) {
            // can only import from other OMT files
            return null;
        }
        boolean sameModule = Optional.ofNullable((importedFile).getModuleName())
                .map(s -> s.equals(file.getModuleName()))
                .orElse(false);
        String importPath;
        if (sameModule) {
            importPath = getRelativePath(file, importedFile);
        } else {
            importPath = getShorthandImport(importedFile);
            if (importPath == null) {
                importPath = getRelativePath(file, importedFile);
            } else {
                importPath = "'" + importPath + "'";
            }
        }
        return importPath != null ? importPath.replace("\\", "/") : null;
    }

    private static OMTFile getImportedFile(PsiCallable psiCallable) {
        PsiFile containingFile = psiCallable.getContainingFile();
        return getImportedFile(containingFile);
    }

    private static OMTFile getImportedFile(PsiFile injectedFile) {
        if (injectedFile instanceof OMTFile) {
            return (OMTFile) injectedFile;
        }

        InjectedLanguageManager instance = InjectedLanguageManager.getInstance(injectedFile.getProject());
        PsiLanguageInjectionHost injectionHost = instance.getInjectionHost(injectedFile);
        if (injectionHost == null) {
            return null;
        }

        PsiFile hostFile = injectionHost.getContainingFile();
        return hostFile instanceof OMTFile ? (OMTFile) hostFile : null;
    }

    private static String getRelativePath(OMTFile fromFile, OMTFile toFile) {
        // do not use the VirtualFile.toNioPath directly, this is not compatible with unit-test
        // virtual files that reside in the temp:/// domain as non-physical files
        Path fromPath = Path.of(fromFile.getVirtualFile().getPath());
        Path toPath = Path.of(toFile.getVirtualFile().getPath());
        String relativePath = fromPath.getParent()
                .relativize(toPath)
                .toString();
        if (!relativePath.startsWith(".")) {
            relativePath = "./" + relativePath;
        }
        return relativePath;
    }

    private static String getShorthandImport(OMTFile importedFile) {
        final SettingsState settingsState = SettingsState.getInstance(importedFile.getProject());
        return settingsState.mappingPaths.entrySet()
                .stream()
                .filter(entry -> isShorthand(entry, importedFile))
                .sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue()))
                .map(entry -> createShorthandImport(entry, importedFile))
                .findFirst()
                .orElse(null);
    }

    private static boolean isShorthand(Map.Entry<String, String> entry, OMTFile importedFile) {
        String filePath = importedFile.getVirtualFile().getPath();
        String shorthandPath = getBasePath(importedFile.getProject()) + "/" + entry.getValue();
        return filePath.startsWith(shorthandPath);
    }

    private static String createShorthandImport(Map.Entry<String, String> entry, OMTFile importedFile) {
        String filePath = importedFile.getVirtualFile().getPath();
        String shorthandPath = getBasePath(importedFile.getProject()) + "/" + entry.getValue();
        return entry.getKey() + filePath.substring(shorthandPath.length());
    }

    private static String getBasePath(Project project) {
        return ApplicationManager.getApplication().isUnitTestMode() ? "/src" : project.getBasePath();
    }

    public static void addImport(OMTFile importingFile, Project project, Editor editor, String importPath, String memberName) {
        YAMLKeyValue newImport = YAMLElementGenerator.getInstance(project).createYamlKeyValue(importPath, "- " + memberName);
        YAMLMapping rootMapping = importingFile.getRootMapping();
        YAMLKeyValue importMap = rootMapping != null ? rootMapping.getKeyValueByKey("import") : null;

        if (rootMapping == null) {
            importingFile.add(createNewImport(newImport, project));
        } else if (importMap == null) {
            PsiElement insertedImportMap = rootMapping.addBefore(createNewImport(newImport, project), rootMapping.getFirstChild());
            PsiElement newLine = PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n");
            rootMapping.addAfter(newLine, insertedImportMap);
            reformatYaml(project, editor, insertedImportMap);
        } else {
            YAMLValue importMapValue = importMap.getValue();
            if (importMapValue instanceof YAMLMapping) {
                YAMLMapping mapping = (YAMLMapping) importMapValue;
                String resolvedPath = resolveToPath(project, importingFile, importPath);
                if (resolvedPath == null) {
                    return;
                }
                YAMLKeyValue existingImport = mapping.getKeyValues().stream()
                        .filter(keyValue -> resolvedPath.equals(resolveToPath(project, importingFile, keyValue.getKeyText())))
                        .findFirst()
                        .orElse(null);
                if (existingImport == null) {
                    // add new import and sequence item:
                    PsiElement insertedImportPath = importMapValue.add(newImport);
                    PsiElement newLine = PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n");
                    importMapValue.addBefore(newLine, insertedImportPath);
                } else {
                    // add sequence item to existing item:
                    YAMLValue value = existingImport.getValue();
                    YAMLSequenceItem sequenceItem = PsiTreeUtil.findChildOfType(newImport, YAMLSequenceItem.class);
                    if (value instanceof YAMLSequence && sequenceItem != null) {
                        PsiElement insertedSequenceItem = value.add(sequenceItem);
                        PsiElement newLine = PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n");
                        value.addBefore(newLine, insertedSequenceItem);
                    }
                }
                reformatYaml(project, editor, importMap);
            }
        }
    }

    private static void reformatYaml(Project project, Editor editor, PsiElement element) {
        CodeStyleManager codeStyleManager = CodeStyleManager.getInstance(project);
        PsiDocumentManager.getInstance(project).doPostponedOperationsAndUnblockDocument(editor.getDocument());
        codeStyleManager.reformat(element);
    }

    private static YAMLKeyValue createNewImport(YAMLKeyValue importEntry, Project project) {
        return YAMLElementGenerator.getInstance(project).createYamlKeyValue("import", importEntry.getText());
    }

    private static IntentionAction getIntentionActions(OMTFile omtFile, PsiCallable callable) {
        String importPath = getImportPath(omtFile, callable);
        if (importPath == null) {
            return null;
        }

        return new IntentionAction() {
            @Override
            public @IntentionName @NotNull String getText() {
                return "Import as " + callable.getType() + " from " + importPath;
            }

            @Override
            public @NotNull @IntentionFamilyName String getFamilyName() {
                return "Import";
            }

            @Override
            public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
                return true;
            }

            @Override
            public void invoke(@NotNull Project project, Editor editor, PsiFile file) throws IncorrectOperationException {
                OMTFile importedFile = getImportedFile(file);
                if (importedFile == null) {
                    return;
                }
                addImport(importedFile, project, editor, importPath, callable.getName());
            }

            @Override
            public boolean startInWriteAction() {
                return true;
            }

        };
    }

    private static String trimPath(Project project, String path) {
        if (path.startsWith("'") || path.startsWith("\"")) {
            return YAMLElementGenerator.getInstance(project).createYamlKeyValue(path, "")
                    .getKeyText();
        }
        return path;
    }

    public static String resolveToPath(Project project, PsiFile containingFile, String path) {
        final SettingsState settingsState = SettingsState.getInstance(project);
        final Collection<String> keySet = settingsState.mappingPaths.keySet();

        // trim any quotes that might be present
        String trimmedPath = trimPath(project, path);

        if (trimmedPath.startsWith(MODULE)) {
            // TODO: resolve module from FS
            // String moduleName = path.substring(MODULE.length());
            return null;
        } else if (keySet.stream().anyMatch(trimmedPath::startsWith)) {
            final Pair<String, String> mapEntry = keySet.stream().sorted(Comparator.reverseOrder())
                    .filter(trimmedPath::startsWith)
                    .map(key -> new Pair<>(key, settingsState.mappingPaths.get(key)))
                    .findFirst()
                    .orElse(null);
            if (mapEntry == null) {
                return trimmedPath;
            }
            String basePath = project.getBasePath();
            String mapped = trimmedPath.replace(mapEntry.getFirst(), mapEntry.getSecond());
            return String.format("%s/%s", basePath, mapped);
        } else {
            return Optional.ofNullable(containingFile)
                    .map(PsiFile::getVirtualFile)
                    .map(VirtualFile::getParent)
                    .map(folder -> folder.findFileByRelativePath(trimmedPath))
                    .map(OMTImportUtil::getResolvablePath)
                    .orElse(null);
        }
    }

    private static String getResolvablePath(VirtualFile virtualFile) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return virtualFile.toString();
        }
        return virtualFile.getPath();
    }
}
