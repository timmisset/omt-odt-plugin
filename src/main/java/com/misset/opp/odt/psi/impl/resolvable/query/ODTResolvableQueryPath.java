package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.callable.Call;
import com.misset.opp.odt.psi.ODTQueryOperationStep;
import com.misset.opp.odt.psi.ODTQueryPath;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.queryStep.ODTResolvableQueryOperationStep;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ODTResolvableQueryPath extends ODTResolvableQuery implements ODTQueryPath {
    public ODTResolvableQueryPath(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(getLastOperation())
                .map(ODTResolvable::resolve)
                .orElse(Collections.emptySet());
    }

    @Override
    public Set<OntResource> resolve(Set<OntResource> resources,
                                    Call call) {
        return Optional.ofNullable(getLastOperation())
                .map(lastStep -> lastStep.resolve(resources, call))
                .orElse(Collections.emptySet());
    }

    private Set<OntResource> fromSet;

    /**
     * Resolves the query with a given input, the provided set is evaluated once,
     * after which the fromSet reset
     */
    public Set<OntResource> resolveFromSet(Set<OntResource> fromSet) {
        this.fromSet = fromSet;
        final Set<OntResource> resolve = resolve();
        this.fromSet = null;
        return resolve;
    }
    public Set<OntResource> getFromSet() {
        return fromSet;
    }

    private ODTResolvableQueryOperationStep getLastOperation() {
        if(getQueryOperationStepList().isEmpty()) { return null; }
        final ArrayList<ODTQueryOperationStep> operations = new ArrayList<>(getQueryOperationStepList());
        Collections.reverse(operations);
        return (ODTResolvableQueryOperationStep) operations.get(0);
    }

    public boolean startsWithDelimiter() {
        return Optional.ofNullable(getFirstChild())
                .map(PsiElement::getNode)
                .map(ASTNode::getElementType)
                .map(ODTTypes.FORWARD_SLASH::equals)
                .orElse(false);
    }

    @Override
    public Set<OntResource> filter(Set<OntResource> resources) {
        // todo: not supported yet
        // possibility: $input[NOT rdf:type == /ont:ClassA]
        return Optional.ofNullable(getLastOperation())
                .map(ODTQueryOperationStep::getQueryStep)
                .map(odtQueryStep -> odtQueryStep.filter(resources))
                .orElse(resources); // by default, return the input
    }

    public List<ODTResolvableQueryOperationStep> getResolvableQueryOperationStepList() {
        return getQueryOperationStepList().stream()
                .map(ODTResolvableQueryOperationStep.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isReference() {
        return getResolvableQueryOperationStepList().size() == 1 &&
                Optional.ofNullable(getLastOperation())
                        .map(ODTQueryOperationStep::getQueryStep)
                        .map(PsiElement::getReference)
                        .map(PsiReference::resolve)
                        .isPresent();
    }
}
