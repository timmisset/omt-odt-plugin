package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.misset.opp.odt.documentation.ODTDocumentationProvider;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.reference.ODTTTLSubjectPredicateReference;
import com.misset.opp.odt.psi.reference.ODTTTLSubjectReference;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * A query step that can be resolved to a fully qualified URI.
 * This can be either a <http://ontology#ClassA> URI or ont:ClassA curie
 * When resolved, it will attempt to traverse the model from the input (subject) to the output (object)
 * using the fully qualified URI as predicate.
 */
public abstract class ODTResolvableQualifiedUriStep extends ODTResolvableQueryStepBase {

    private static final Key<CachedValue<String>> FULLY_QUALIFIED_URI = new Key<>("FULLY_QUALIFIED_URI");

    public ODTResolvableQualifiedUriStep(@NotNull ASTNode node) {
        super(node);
    }

    public abstract String calculateFullyQualifiedUri();

    public String getFullyQualifiedUri() {
        return CachedValuesManager.getCachedValue(this,
                FULLY_QUALIFIED_URI,
                () -> new CachedValueProvider.Result<>(calculateFullyQualifiedUri(),
                        getContainingFile(),
                        PsiModificationTracker.MODIFICATION_COUNT));
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        inspectIri(holder);
        if (!isPartOfReverseStep()) {
            inspectResolved(holder, "FORWARD");
        }
    }

    @Override
    public String getDocumentation() {
        boolean isReversed = getParent() instanceof ODTQueryReverseStep;
        Set<OntResource> unfiltered = isReversed ? ((ODTResolvableQueryStep) getParent()).resolve() : resolve();
        Set<OntResource> filtered = getResolvableParent().filter(unfiltered);
        String fullyQualifiedUri = getFullyQualifiedUri();
        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append("Predicate<br>");
        sb.append(fullyQualifiedUri);
        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        final String content;
        if (isReversed) {
            content = "When traversing the model <u>reversed</u>, the query will return anything in the TTL ontology which " +
                    "has the predicate (sh:path) and has the previous query step as object (sh:dataType / sh:class)";
        } else {
            content = "When traversing the model, the query will return the object (sh:dataType / sh:class) of the " +
                    "previous query step (subject) using the predicate (sh:path)";
        }
        sb.append(content);
        sb.append(DocumentationMarkup.CONTENT_END);
        sb.append(DocumentationMarkup.SECTIONS_START);

        ODTDocumentationProvider.addKeyValueSection("Direction:", isReversed ? "Reverse" : "Forward", sb);

        Set<OntResource> previousStep = resolvePreviousStep();
        if (!previousStep.isEmpty()) {
            String label = isReversed ? "Object:" : "Subject:";
            ODTDocumentationProvider.addKeyValueSection(label, TTLResourceUtil.describeUrisJoined(previousStep, "<br>"), sb);
        }
        if (OppModel.INSTANCE.getProperty(fullyQualifiedUri) != null) {
            String label = isReversed ? "Subject:" : "Object:";
            ODTDocumentationProvider.addKeyValueSection(label, TTLResourceUtil.describeUrisJoined(filtered, "<br>"), sb);
            if (unfiltered.size() != filtered.size()) {
                ODTDocumentationProvider.addKeyValueSection("Unfiltered:", TTLResourceUtil.describeUrisJoined(unfiltered, "<br>"), sb);
            }
        }


        sb.append(DocumentationMarkup.SECTIONS_END);
        return sb.toString();
    }

    private void inspectIri(ProblemsHolder holder) {
        final String fullyQualifiedUri = getFullyQualifiedUri();
        if (fullyQualifiedUri != null) {
            final Resource resource = OppModel.INSTANCE.getOntResource(fullyQualifiedUri, getProject());
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
    public void annotate(AnnotationHolder holder) {
        if (!isPartOfReverseStep()) {
            super.annotate(holder);
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
