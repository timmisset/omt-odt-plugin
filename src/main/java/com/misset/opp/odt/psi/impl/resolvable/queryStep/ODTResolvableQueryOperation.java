package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.misset.opp.odt.psi.ODTQueryOperation;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQueryPath;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class ODTResolvableQueryOperation extends ASTWrapperPsiElement implements ODTQueryOperation, ODTResolvable {
    public ODTResolvableQueryOperation(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public ODTResolvableQueryPath getParent() {
        return (ODTResolvableQueryPath) super.getParent();
    }

    protected boolean isFirstStepInPath() {
        return this.equals(getParent().getQueryOperationList().get(0));
    }

    protected boolean isRootStep() {
        return getParent().startsWithDelimiter() && isFirstStepInPath();
    }

    protected ODTResolvableQueryOperation getPreviousOperation() {
        if(isFirstStepInPath()) {
            // check if inside container, in which case, resolve to the step preceding the container
            // for example:
            // /ont:ClassA / ^rdf:type[rdf:type == /ont:ClassA]
            // the rdf:type in the filter should return the outcome of the ^rdf:type
            return null;
        } else {
            final List<ODTQueryOperation> queryOperationList = getParent().getQueryOperationList();
            int index = queryOperationList.indexOf(this);
            return (ODTResolvableQueryOperation) queryOperationList.get(index -1);
        }
    }

    @Override
    public Set<OntResource> resolve() {
        if(getQueryStep() != null) {
            return getQueryStep().resolve();
        }
        return Collections.emptySet();
    }
}
