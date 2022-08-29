package com.misset.opp.odt.psi.impl.resolvable.querystep.choose;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTChooseBlock;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableQueryStepBase;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ODTResolvableChooseBlockStep extends ODTResolvableQueryStepBase implements ODTChooseBlock {
    protected ODTResolvableChooseBlockStep(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return getQueryStepList().stream()
                .map(Resolvable::resolve)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    protected PsiElement getAnnotationRange() {
        return getFirstChild();
    }

    public List<ODTResolvableWhenPathStep> getWhenPathList() {
        return getQueryStepList().stream()
                .filter(odtQueryStep -> odtQueryStep.getNode().getElementType() == ODTTypes.WHEN_PATH)
                .map(ODTResolvableWhenPathStep.class::cast).collect(Collectors.toList());
    }

    public ODTResolvableOtherwisePathStep getOtherwisePath() {
        return getQueryStepList().stream()
                .filter(odtQueryStep -> odtQueryStep.getNode().getElementType() == ODTTypes.OTHERWISE_PATH)
                .map(ODTResolvableOtherwisePathStep.class::cast)
                .findFirst()
                .orElse(null);
    }
}
