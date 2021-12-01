package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.misset.opp.omt.meta.OMTImportMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTIriMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.references.OMTImportPathReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.Optional;

/**
 * The OMT key:value pairs of interest are the ones that are used for prefix declarations,
 * the imports (path: members as sequence value) and the ModelItems.
 */
@OMTOverride
public class YAMLOMTKeyValueImpl extends YAMLKeyValueImpl {
    public YAMLOMTKeyValueImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        if (getKey() != null && getMetaType() instanceof OMTImportMetaType) {
            return new OMTImportPathReference(this, getKey().getTextRangeInParent());
        }
        return null;
    }

    @Override
    public PsiReference[] getReferences() {
        return Optional.ofNullable(getReference())
                .stream()
                .toArray(PsiReference[]::new);
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        if (getMetaType() instanceof OMTIriMetaType) {
            return GlobalSearchScope.fileScope(getContainingFile());
        } else if (getValueMetaType() instanceof OMTModelItemMetaType) {
            return ((OMTFile) getContainingFile()).getMemberUsageScope(true);
        }
        return super.getUseScope();
    }

    private YamlMetaType getMetaType() {
        return OMTMetaTypeProvider.getInstance(getProject()).getResolvedMetaType(this);
    }

    private YamlMetaType getValueMetaType() {
        return OMTMetaTypeProvider.getInstance(getProject()).getResolvedKeyValueMetaTypeMeta(this);
    }
}
