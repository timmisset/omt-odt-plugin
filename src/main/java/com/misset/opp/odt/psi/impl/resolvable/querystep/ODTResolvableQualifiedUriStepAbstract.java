package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.documentation.ODTUriStepDocumentationUtil;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.reference.ODTTTLSubjectPredicateReference;
import com.misset.opp.odt.psi.reference.ODTTTLSubjectReference;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQualifiedUriStep;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.jetbrains.annotations.NotNull;

/**
 * A query step that can be resolved to a fully qualified URI.
 * This can be either a <http://ontology#ClassA> URI or ont:ClassA curie
 * When resolved, it will attempt to traverse the model from the input (subject) to the output (object)
 * using the fully qualified URI as predicate.
 */
public abstract class ODTResolvableQualifiedUriStepAbstract extends ODTResolvableQueryStepAbstract implements ODTResolvableQualifiedUriStep {
    private static final Key<CachedValue<String>> FULLY_QUALIFIED_URI = new Key<>("FULLY_QUALIFIED_URI");

    protected ODTResolvableQualifiedUriStepAbstract(@NotNull ASTNode node) {
        super(node);
    }

    protected abstract String calculateFullyQualifiedUri();

    @Override
    public String getFullyQualifiedUri() {
        PsiFile containingFile = getContainingFile();
        if (containingFile == null) {
            return null;
        }
        return CachedValuesManager.getCachedValue(this,
                FULLY_QUALIFIED_URI,
                () -> new CachedValueProvider.Result<>(calculateFullyQualifiedUri(),
                        containingFile,
                        PsiModificationTracker.MODIFICATION_COUNT));
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        inspectIri(holder);
        if (!isPartOfReverseStep()) {
            inspectResolved(holder, "FORWARD");
        }
    }

    protected void inspectResolved(ProblemsHolder holder,
                                   String direction) {
        if (resolve().isEmpty() && !resolvePreviousStep().isEmpty()) {
            final String fullyQualifiedUri = getFullyQualifiedUri();
            if (fullyQualifiedUri != null) {
                holder.registerProblem(this,
                        "Could not traverse " + direction + " using predicate: " + fullyQualifiedUri);
            }
        }
    }

    @Override
    public String getDocumentation(Project project) {
        ODTUriStepDocumentationUtil documentationUtil = ODTUriStepDocumentationUtil.getInstance(project);
        if (documentationUtil == null) {
            return "no documentation found";
        }
        return documentationUtil.getDocumentation(this);
    }

    private void inspectIri(ProblemsHolder holder) {
        final String fullyQualifiedUri = getFullyQualifiedUri();
        if (fullyQualifiedUri != null) {
            final Resource resource = OntologyModel.getInstance(getProject()).getOntResource(fullyQualifiedUri, getProject());
            if (resource == null) {
                // unknown Iri
                holder.registerProblem(
                        this,
                        "Could not find resource <" + fullyQualifiedUri + "> in the Opp Model"
                );
            }
        }
    }

    @Override
    public PsiReference getReference() {
        if (getParent() instanceof ODTQueryReverseStep) {
            return null;
        }
        if (isRootStep()) {
            // reference a TTL Subject
            return new ODTTTLSubjectReference(this, getModelReferenceTextRange());
        } else {
            // reference a TTL Predicate
            return new ODTTTLSubjectPredicateReference(this, getModelReferenceTextRange());
        }
    }

    @Override
    public String getNamespace() {
        return ResourceFactory.createResource(getFullyQualifiedUri()).getNameSpace();
    }

    @Override
    public String getLocalName() {
        return ResourceFactory.createResource(getFullyQualifiedUri()).getLocalName();
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null;
    }
}
