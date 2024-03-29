package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.resolvable.Variable;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;

import java.util.List;
import java.util.Set;

public interface ODTVariableDelegate {
    boolean isDeclaredVariable();

    boolean canBeDeclaredVariable(ODTVariable variable);

    ODTVariable getElement();

    boolean isAssignedVariable();

    PsiReference getReference();

    Set<OntResource> resolve();

    List<Literal> resolveLiteral();

    Variable getDeclared();

    void delete();

    String getSource();
}
