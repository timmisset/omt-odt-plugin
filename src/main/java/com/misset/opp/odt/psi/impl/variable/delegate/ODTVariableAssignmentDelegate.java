package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.psi.ODTDeclareVariable;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.reference.ODTVariableReference;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ODTVariableAssignmentDelegate extends ODTDeclaredVariableDelegate {

    public ODTVariableAssignmentDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public boolean isDeclaredVariable() {
        if (isAssignmentPart()) {
            return false;
        }
        return isOMTVariableProvider() || Optional.ofNullable(element.getParent())
                .map(PsiElement::getParent)
                .map(ODTDeclareVariable.class::isInstance)
                .orElse(Boolean.FALSE);
    }

    @Override
    public Set<OntResource> getType() {
        // (VAR) $variableA, $variableB = @SOME_COMMAND();
        if (isDeclaredVariable()) {
            final ODTVariableAssignment variableAssignment = PsiTreeUtil.getParentOfType(element,
                    ODTVariableAssignment.class);
            final List<ODTVariable> variableList = variableAssignment.getVariableList();
            final int i = variableList.indexOf(element);
            final ODTVariableValue variableValue = variableAssignment.getVariableValue();
            if (i == 0) {
                if (variableValue.getQuery() != null) {
                    return variableValue.getQuery().resolve();
                } else if (variableValue.getCommandCall() != null) {
                    return variableValue.getCommandCall().resolve();
                }
            } else if (i == 1) {
                return Optional.ofNullable(variableValue.getCommandCall())
                        .map(ODTCall::getCallable)
                        .map(Callable::getSecondReturnArgument)
                        .orElse(Collections.emptySet());
            }
        }
        return Collections.emptySet();
    }

    @Override
    public PsiReference getReference() {
        return !isDeclaredVariable() ? new ODTVariableReference(element) : null;
    }
}
