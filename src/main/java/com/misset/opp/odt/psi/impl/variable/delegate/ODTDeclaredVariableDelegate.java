package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.refactoring.ODTRefactoringUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Set;

/**
 * VAR $variable;
 */
public class ODTDeclaredVariableDelegate extends ODTVariableDelegateAbstract {

    public ODTDeclaredVariableDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public boolean isDeclaredVariable() {
        return true;
    }

    @Override
    public PsiReference getReference() {
        return null;
    }

    @Override
    public Set<OntResource> resolve() {
        return Collections.emptySet();
    }

    @Override
    public void delete() {
        ODTRefactoringUtil.removeScriptline(element);
    }
}
