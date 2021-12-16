package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQualifiedUriStep;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ODTTTLPredicateObjectReference extends PsiReferenceBase.Poly<ODTResolvableQualifiedUriStep> implements PsiPolyVariantReference {
    public ODTTTLPredicateObjectReference(ODTResolvableQualifiedUriStep element, TextRange textRange) {
        super(element, textRange, false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        ODTResolvableQualifiedUriStep element = getElement();
        Set<OntResource> previousStep = element.resolvePreviousStep();
        String fullyQualifiedUri = element.getFullyQualifiedUri();
        return ResolveResult.EMPTY_ARRAY;
    }

}
