package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTQueryOperation;
import com.misset.opp.odt.psi.ODTQueryPath;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class ODTResolvableQueryPath extends ODTResolvableQuery implements ODTQueryPath {
    public ODTResolvableQueryPath(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public Set<OntResource> resolve() {
        /*
            Resolve by resolving the last query step of the path
         */
        final ArrayList<ODTQueryOperation> operations = new ArrayList<>(getQueryOperationList());
        Collections.reverse(operations);

        return operations
                .stream()
                .map(ODTQueryOperation::getQueryStep)
                .filter(Objects::nonNull)
                .map(ODTResolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
    }

    public boolean startsWithDelimiter() {
        return Optional.ofNullable(getFirstChild())
                .map(PsiElement::getNode)
                .map(ASTNode::getElementType)
                .map(ODTTypes.FORWARD_SLASH::equals)
                .orElse(false);
    }
}
