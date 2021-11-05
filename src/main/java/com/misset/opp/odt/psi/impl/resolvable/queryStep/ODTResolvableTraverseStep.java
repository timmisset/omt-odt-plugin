package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;

/**
 * Forward model traversion
 * Requires a fully qualified URI as predicate to traverse from the previous step (subject) into the next (object)
 */
public abstract class ODTResolvableTraverseStep extends ODTResolvableQueryStep {

    public ODTResolvableTraverseStep(@NotNull ASTNode node) {
        super(node);
    }
    abstract String getFullyQualifiedUri();
}
