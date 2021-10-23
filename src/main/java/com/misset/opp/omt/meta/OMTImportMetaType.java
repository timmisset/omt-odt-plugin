package com.misset.opp.omt.meta;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.misset.opp.omt.meta.arrays.OMTImportPathMetaType;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Optional;

import static com.misset.opp.util.ImportUtil.getOMTFile;

public class OMTImportMetaType extends OMTMetaMapType {
    private static final String MODULE = "module:";
    protected OMTImportMetaType() {
        super("OMT Import");
    }

    @Override
    protected YamlMetaType getMapEntryType(String name) {
        return new OMTImportPathMetaType(name);
    }

    public OMTFile resolveToOMTFile(YAMLKeyValue keyValue) {
        return Optional.ofNullable(resolveToPath(keyValue))
                .map(path -> getOMTFile(path, keyValue.getProject()))
                .orElse(null);
    }

    public String resolveToPath(YAMLKeyValue keyValue) {
        String path = keyValue.getKeyText();
        if (path.startsWith(MODULE)) {
            // TODO: resolve module from FS
            // String moduleName = path.substring(MODULE.length());
            return null;
        } else if (path.startsWith("@client")) {
            String basePath = keyValue.getProject().getBasePath();
            String mapped = path.replace("@client", "frontend/libs");
            return String.format("%s/%s", basePath, mapped);
        } else {
            return Optional.ofNullable(keyValue.getContainingFile())
                    .map(PsiFile::getVirtualFile)
                    .map(VirtualFile::getParent)
                    .map(folder -> folder.findFileByRelativePath(path))
                    .map(VirtualFile::toString)
                    .orElse(null);
        }
    }
}
