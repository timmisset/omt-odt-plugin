package com.misset.opp.odt.psi.impl.resolvable.querystep;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.reference.ODTTTLSubjectPredicateReference;
import com.misset.opp.odt.psi.reference.ODTTTLSubjectReference;
import com.misset.opp.ttl.model.OppModel;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.jetbrains.annotations.NotNull;

/**
 * A query step that can be resolved to a fully qualified URI.
 * This can be either a <http://ontology#ClassA> URI or ont:ClassA curie
 * When resolved, it will attempt to traverse the model from the input (subject) to the output (object)
 * using the fully qualified URI as predicate.
 */
public abstract class ODTResolvableQualifiedUriStep extends ODTResolvableQueryStepBase {

    private static final Key<CachedValue<String>> FULLY_QUALIFIED_URI = new Key<>("FULLY_QUALIFIED_URI");

    protected ODTResolvableQualifiedUriStep(@NotNull ASTNode node) {
        super(node);
    }

    public abstract String calculateFullyQualifiedUri();

    @Override
    public String getFullyQualifiedUri() {
        return CachedValuesManager.getCachedValue(this,
                FULLY_QUALIFIED_URI,
                () -> new CachedValueProvider.Result<>(calculateFullyQualifiedUri(),
                        getODTFile(),
                        PsiModificationTracker.MODIFICATION_COUNT));
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        inspectIri(holder);
        if (!isPartOfReverseStep()) {
            super.inspect(holder);
            inspectResolved(holder, "FORWARD");
        }
    }

    @Override
    public String getDocumentation(Project project) {
        return ODTResolvableQualifiedUriStepDocumentationUtil.getDocumentation(this);
    }

    private void inspectIri(ProblemsHolder holder) {
        final String fullyQualifiedUri = getFullyQualifiedUri();
        if (fullyQualifiedUri != null) {
            final Resource resource = OppModel.getInstance().getOntResource(fullyQualifiedUri, getProject());
            if (resource == null) {
                // unknown Iri
                holder.registerProblem(
                        this,
                        "Could not find resource <" + fullyQualifiedUri + "> in the Opp Model"
                );
            }
        }
    }

    /**
     * Return the TextRange that should be used for the reference to the TTL model
     */
    protected abstract TextRange getModelReferenceTextRange();

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

    public String getNamespace() {
        return ResourceFactory.createResource(getFullyQualifiedUri()).getNameSpace();
    }

    public String getLocalName() {
        return ResourceFactory.createResource(getFullyQualifiedUri()).getLocalName();
    }
}
