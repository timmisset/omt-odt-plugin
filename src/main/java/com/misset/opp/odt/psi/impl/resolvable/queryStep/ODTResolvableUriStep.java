package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * A query step that can be resolved to a fully qualified URI.
 * This can be either a <http://ontology#ClassA> URI or ont:ClassA curie
 * When resolved, it will attempt to traverse the model from the input (subject) to the output (object)
 * using the fully qualified URI as predicate.
 */
public abstract class ODTResolvableUriStep extends ODTResolvableQueryStep {

    public ODTResolvableUriStep(@NotNull ASTNode node) {
        super(node);
    }
    public abstract String getFullyQualifiedUri();
}
