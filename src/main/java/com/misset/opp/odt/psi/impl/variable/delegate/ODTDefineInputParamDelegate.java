package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.documentation.ODTDocumentationUtil;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 * VAR $variable;
 */
public class ODTDefineInputParamDelegate extends ODTDeclaredVariableDelegate {

    private static final Key<CachedValue<Set<OntResource>>> ONT_TYPE = new Key<>("ONT_TYPE");

    public ODTDefineInputParamDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public Set<OntResource> getType() {
        return CachedValuesManager.getCachedValue(element, ONT_TYPE, () ->
                new CachedValueProvider.Result<>(calculateType(),
                        element.getContainingFile(),
                        OppModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER));
    }

    private PsiElement getScope() {
        return Optional.ofNullable(PsiTreeUtil.getParentOfType(element, ODTScript.class))
                .map(PsiElement::getParent)
                .orElse(null);
    }

    private Set<OntResource> calculateType() {
        PsiElement scope = getScope();
        if(scope == null) { return Collections.emptySet(); }
        final PsiDocTag docTag = ReadAction.compute(() -> ReferencesSearch.search(element,
                        new LocalSearchScope(scope))
                .mapping(PsiReference::getElement)
                .filtering(PsiDocTag.class::isInstance)
                .mapping(PsiDocTag.class::cast)
                .findFirst());
        return ODTDocumentationUtil.getTypeFromDocTag(docTag, 1);
    }
}
