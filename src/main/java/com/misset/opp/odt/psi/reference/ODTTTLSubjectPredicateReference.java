package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.stubs.StubIndex;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.util.OntologyScopeUtil;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQualifiedUriStep;
import com.misset.opp.ttl.psi.TTLLocalname;
import com.misset.opp.ttl.psi.TTLObject;
import com.misset.opp.ttl.stubs.index.TTLObjectStubIndex;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ODTTTLSubjectPredicateReference extends PsiReferenceBase.Poly<ODTResolvableQualifiedUriStep> implements PsiPolyVariantReference {
    public ODTTTLSubjectPredicateReference(ODTResolvableQualifiedUriStep element, TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        String fullyQualifiedUri = myElement.getFullyQualifiedUri();
        if (fullyQualifiedUri == null) {
            return ResolveResult.EMPTY_ARRAY;
        }
        Set<OntResource> previousStep = myElement.resolvePreviousStep();
        List<String> acceptableSubjectClasses = previousStep.stream()
                .map(OntologyModel.getInstance()::listOntClasses)
                .flatMap(Collection::stream)
                .map(Resource::getURI)
                .collect(Collectors.toList());
        return StubIndex.getElements(
                        TTLObjectStubIndex.KEY,
                        myElement.getFullyQualifiedUri(),
                        myElement.getProject(),
                        OntologyScopeUtil.getModelSearchScope(myElement.getProject()),
                        TTLObject.class
                ).stream()
                .filter(TTLObject::isPredicate)
                .filter(ttlObject ->
                        // when the previous step cannot be resolved, resolve to every class in the model
                        // that has this predicate. The user can select one when multiple options are available
                        acceptableSubjectClasses.isEmpty() || acceptableSubjectClasses.contains(ttlObject.getSubjectIri()))
                .map(PsiElementResolveResult::new)
                .toArray(ResolveResult[]::new);
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        if (element instanceof TTLLocalname) {
            String fullyQualifiedUri = myElement.getFullyQualifiedUri();
            return fullyQualifiedUri != null &&
                    fullyQualifiedUri.equals(((TTLLocalname) element).getQualifiedIri());
        }
        return super.isReferenceTo(element);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return myElement.setName(newElementName);
    }
}
