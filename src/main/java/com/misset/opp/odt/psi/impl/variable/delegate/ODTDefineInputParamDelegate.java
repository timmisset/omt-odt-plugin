package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.search.LocalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.*;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.odt.psi.impl.prefix.PrefixUtil;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValueParserUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * VAR $variable;
 */
public class ODTDefineInputParamDelegate extends ODTDeclaredVariableDelegate {
    private static final Pattern LOCAL_NAME = Pattern.compile("\\([^:]*:([^)]*)\\)");
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
        if (docTag != null && docTag.getDataElements().length == 2) {
            // we can retrieve the type from the DocTag:
            final PsiElement dataElement = docTag.getDataElements()[1];
            final PsiReference[] references = docTag.getReferences();
            final Optional<PsiReference> curieReference = Arrays.stream(references)
                    .filter(psiReference -> psiReference.getRangeInElement()
                            .intersects(dataElement.getTextRangeInParent()))
                    .findFirst();
            if (curieReference.isPresent()) {
                // there is reference available for the type, meaning we should try to resolve it to the prefix
                // and generate a fully qualified URI from it:
                final PsiElement prefix = curieReference.get().resolve();
                if (prefix instanceof YAMLKeyValue) {
                    final Matcher matcher = LOCAL_NAME.matcher(dataElement.getText());
                    if (matcher.find()) {
                        return Optional.ofNullable(PrefixUtil.getFullyQualifiedUri((YAMLKeyValue) prefix,
                                        matcher.group(1)))
                                .map(OppModel.INSTANCE::getClassIndividuals)
                                .stream()
                                .flatMap(Collection::stream)
                                .collect(Collectors.toSet());
                    }
                }
            } else {
                // no curie reference, probably a primitive type:
                // (string)
                final String value = dataElement.getText().replaceAll("[()]", "");
                return Optional.ofNullable(TTLValueParserUtil.parsePrimitive(value))
                        .map(OntResource.class::cast)
                        .map(Set::of)
                        .orElse(Collections.emptySet());
            }
        }
        return Collections.emptySet();
    }
}
