package com.misset.opp.omt.psi.impl.yaml;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.meta.OMTImportMemberMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.OMTOntologyPrefixMetaType;
import com.misset.opp.omt.meta.model.scalars.OMTParamTypeType;
import com.misset.opp.omt.meta.model.variables.OMTNamedVariableMetaType;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegateFactory;
import com.misset.opp.omt.psi.references.OMTImportMemberReference;
import com.misset.opp.omt.psi.references.OMTOntologyPrefixReference;
import com.misset.opp.omt.psi.references.OMTParamTypeReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Optional;

/**
 * By overriding the default YAMLPlainTextImpl we can implement PsiNamedElement and use the
 * refactor-rename, find-usage etc.
 */
@OMTOverride
public class YAMLOMTPlainTextImpl extends YAMLPlainTextImpl implements PsiNamedElement {
    public YAMLOMTPlainTextImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return GlobalSearchScope.fileScope(getContainingFile());
    }

    @Override
    public PsiReference getReference() {
        final YamlMetaType metaType = OMTMetaTypeProvider.getInstance(getProject()).getResolvedMetaType(this);
        if (metaType instanceof OMTImportMemberMetaType) {
            return new OMTImportMemberReference(this);
        } else if (metaType instanceof OMTOntologyPrefixMetaType) {
            return new OMTOntologyPrefixReference(this);
        } else if (metaType instanceof OMTParamMetaType) {
            final TextRange typePrefixRange = ((OMTParamMetaType) metaType).getTypePrefixRange(this);
            if (typePrefixRange != null) {
                return new OMTParamTypeReference(this, typePrefixRange);
            }
        } else if (metaType instanceof OMTParamTypeType) {
            return new OMTParamTypeReference(this,
                    TextRange.allOf(this.getText()));
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
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        final YamlMetaType metaType = OMTMetaTypeProvider.getInstance(getProject()).getResolvedMetaType(this);
        if (metaType instanceof OMTNamedVariableMetaType) {
            final String finalName = !name.startsWith("$") ? "$" + name : name;
            return OMTYamlDelegateFactory.createDelegate(this).setName(finalName);
        }
        return this;
    }

    @Override
    public String getName() {
        final YamlMetaType metaType = OMTMetaTypeProvider.getInstance(getProject()).getResolvedMetaType(this);
        if (metaType instanceof OMTNamedVariableMetaType) {
            return OMTYamlDelegateFactory.createDelegate(this).getName();
        }
        return super.getName();
    }
}
