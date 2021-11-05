package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTQueryOperation;
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
    public Set<OntResource> resolve() {
        return Optional.ofNullable(getLastOperation())
                .map(ODTResolvable::resolve)
                .orElse(Collections.emptySet());
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
                .map(ODTQueryOperation::getQueryStep)
                .map(odtQueryStep -> odtQueryStep.filter(resources))
                .orElse(Collections.emptySet());
    }

    public List<ODTResolvableQueryOperationStep> getResolvableQueryOperationStepList() {
        return getQueryOperationStepList().stream().map(ODTResolvableQueryOperationStep.class::cast).collect(Collectors.toList());
    }
}
