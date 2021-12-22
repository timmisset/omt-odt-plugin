package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.misset.opp.omt.OMTYamlReferenceContributor;
import com.misset.opp.omt.indexing.OMTImportedMembersIndex;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.meta.module.OMTDeclareMetaType;
import com.misset.opp.omt.meta.module.OMTDeclaredModuleMetaType;
import com.misset.opp.omt.meta.scalars.OMTIriMetaType;
import com.misset.opp.omt.psi.references.OMTImportPathReference;
import com.misset.opp.omt.psi.references.OMTModuleExportReference;
import com.misset.opp.omt.psi.references.OMTModuleReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The OMT key:value pairs of interest are the ones that are used for prefix declarations,
 * the imports (path: members as sequence value) and the ModelItems.
 * <p>
 * Since YAMLKeyValueImpl is a ContributedReferenceHost, its getReferences() isn't called, but rather
 * it expects to always get the references via the ReferencesProvidersRegistry
 *
 * @see OMTYamlReferenceContributor
 */
@OMTOverride
public class YAMLOMTKeyValueImpl extends YAMLKeyValueImpl {
    public YAMLOMTKeyValueImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        if (getKey() == null) {
            return null;
        }
        YamlMetaType metaType = getMetaType();
        if (metaType instanceof OMTImportMetaType) {
            return new OMTImportPathReference(this, getKey().getTextRangeInParent());
        } else if (metaType instanceof OMTDeclareMetaType) {
            return new OMTModuleReference(this, getKey().getTextRangeInParent());
        } else if (metaType instanceof OMTDeclaredModuleMetaType) {
            return new OMTModuleExportReference(this, getKey().getTextRangeInParent());
        }
        return null;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        return Optional.ofNullable(getReference())
                .stream()
                .toArray(PsiReference[]::new);
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        if (getMetaType() instanceof OMTIriMetaType) {
            return GlobalSearchScope.fileScope(getContainingFile());
        } else if (getValueMetaType() instanceof OMTModelItemMetaType) {
            return getModelItemSearchScope();
        }
        return super.getUseScope();
    }

    private SearchScope getModelItemSearchScope() {
        final ArrayList<PsiFile> psiFiles = new ArrayList<>();
        psiFiles.add(getContainingFile());
        psiFiles.addAll(OMTImportedMembersIndex.getImportingFiles(getName()));
        final List<VirtualFile> targetFiles = psiFiles.stream().map(PsiFile::getVirtualFile)
                .filter(Objects::nonNull).collect(Collectors.toList());
        return GlobalSearchScope.filesScope(getProject(), targetFiles);
    }

    private YamlMetaType getMetaType() {
        return OMTMetaTypeProvider.getInstance(getProject()).getResolvedMetaType(this);
    }

    private YamlMetaType getValueMetaType() {
        return OMTMetaTypeProvider.getInstance(getProject()).getResolvedKeyValueMetaTypeMeta(this);
    }
}
