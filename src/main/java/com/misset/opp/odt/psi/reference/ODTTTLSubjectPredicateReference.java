package com.misset.opp.odt.psi.reference;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQualifiedUriStep;
import com.misset.opp.ttl.psi.TTLLocalname;
import com.misset.opp.util.TTLElementFinderUtil;
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
        Set<OntResource> previousStep = myElement instanceof ODTQueryReverseStep ?
                myElement.resolve() :
                myElement.resolvePreviousStep();
        List<String> acceptableSubjectClasses = previousStep.stream()
                .map(OntologyModel.getInstance(myElement.getProject())::listOntClasses)
                .flatMap(Collection::stream)
                .map(Resource::getURI)
                .collect(Collectors.toList());
        return TTLElementFinderUtil.getObjectResolveResult(myElement.getProject(), myElement.getFullyQualifiedUri(),
                ttlObject -> acceptableSubjectClasses.isEmpty() || acceptableSubjectClasses.contains(ttlObject.getSubjectIri()));
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
