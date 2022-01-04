package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.stubs.StubIndex;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQualifiedUriStep;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.psi.spo.TTLSPO;
import com.misset.opp.ttl.stubs.index.TTLObjectStubIndex;
import com.misset.opp.ttl.util.TTLScopeUtil;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ODTTTLSubjectPredicateReference extends PsiReferenceBase.Poly<ODTResolvableQualifiedUriStep> implements PsiPolyVariantReference {
    Logger LOGGER = Logger.getInstance(ODTTTLSubjectPredicateReference.class);

    public ODTTTLSubjectPredicateReference(ODTResolvableQualifiedUriStep element, TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return LoggerUtil.computeWithLogger(LOGGER, "Resolving ODTTTLSubjectPredicateReference", () -> {
            ODTResolvableQualifiedUriStep element = getElement();
            String fullyQualifiedUri = element.getFullyQualifiedUri();
            if (fullyQualifiedUri == null) {
                return ResolveResult.EMPTY_ARRAY;
            }
            Set<OntResource> previousStep = element.resolvePreviousStep();
            List<String> acceptableSubjectClasses = previousStep.stream()
                    .map(OppModel.INSTANCE::listOntClasses)
                    .flatMap(Collection::stream)
                    .map(Resource::getURI)
                    .collect(Collectors.toList());
            return StubIndex.getElements(
                            TTLObjectStubIndex.KEY,
                            element.getFullyQualifiedUri(),
                            element.getProject(),
                            TTLScopeUtil.getModelSearchScope(myElement.getProject()),
                            TTLObject.class
                    ).stream()
                    .filter(TTLSPO::isPredicate)
                    .filter(ttlObject ->
                            // when the previous step cannot be resolved, resolve to every class in the model
                            // that has this predicate. The user can select one when multiple options are available
                            acceptableSubjectClasses.isEmpty() || acceptableSubjectClasses.contains(ttlObject.getSubjectIri()))
                    .map(PsiElementResolveResult::new)
                    .toArray(ResolveResult[]::new);
        });
    }
}
