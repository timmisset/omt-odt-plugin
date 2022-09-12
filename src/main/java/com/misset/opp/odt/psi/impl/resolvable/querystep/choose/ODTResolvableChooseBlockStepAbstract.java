package com.misset.opp.odt.psi.impl.resolvable.querystep.choose;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.psi.ODTChooseBlock;
import com.misset.opp.odt.psi.ODTOtherwisePath;
import com.misset.opp.odt.psi.ODTTypes;
import com.misset.opp.odt.psi.ODTWhenPath;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableQueryStepAbstract;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ODTResolvableChooseBlockStepAbstract extends ODTResolvableQueryStepAbstract implements ODTChooseBlock {
    protected ODTResolvableChooseBlockStepAbstract(@NotNull ASTNode node) {
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
    public PsiElement getAnnotationRange() {
        return getFirstChild();
    }

    @Override
    public List<ODTWhenPath> getWhenPathList() {
        return getQueryStepList().stream()
                .filter(odtQueryStep -> odtQueryStep.getNode().getElementType() == ODTTypes.WHEN_PATH)
                .map(ODTWhenPath.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public ODTOtherwisePath getOtherwisePath() {
        return getQueryStepList().stream()
                .filter(odtQueryStep -> odtQueryStep.getNode().getElementType() == ODTTypes.OTHERWISE_PATH)
                .map(ODTOtherwisePath.class::cast)
                .findFirst()
                .orElse(null);
    }
}
