package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.util.Key;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;

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
                        PsiModificationTracker.MODIFICATION_COUNT));
    }

    @Override
    public void inspect(ProblemsHolder holder) {
        inspectIri(holder);
        if (!isPartOfReverseStep()) {
            inspectResolved(holder, "FORWARD");
        }
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


}
