package com.misset.opp.odt.psi.resolvable.querystep;

import com.misset.opp.odt.psi.ODTQueryPath;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface ODTResolvableQueryOperationStep extends ODTResolvable {

    /**
     * Returns true if the current operation contains the root ODTQueryStep
     */
    boolean isRootStep();

    /**
     * Return the parent element
     * Overrides return type from PsiElement.getParent() since it's always an ODTQueryPath
     */
    ODTQueryPath getParent();

    /**
     * Resolves the operation step without applying the filter (if present)
     */
    Set<OntResource> resolveWithoutFilter();

    /**
     * Resolves the preceding step in the parent ODTQueryPath
     */
    Set<OntResource> resolvePreviousStep(Set<OntResource> resources, @Nullable PsiCall call);

}
