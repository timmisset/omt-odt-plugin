package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDeclareVariable;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.reference.ODTVariableReference;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Variable;
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
        return !isAssignmentPart() &&
                PsiTreeUtil.getParentOfType(element, ODTDeclareVariable.class) != null;
    }

    @Override
    public Variable getDeclared() {
        return isDeclaredVariable() ? element : Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .map(this::getWrapper)
                .orElse(null);
    }

    @Override
    public boolean isAssignedVariable() {
        return !isAssignmentPart();
    }

    @Override
    public Set<OntResource> resolve() {
        // (VAR) $variableA, $variableB = @SOME_COMMAND();
        final ODTVariableAssignment variableAssignment = PsiTreeUtil.getParentOfType(element,
                ODTVariableAssignment.class);
        if (variableAssignment == null) {
            return Collections.emptySet();
        }

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
        return Collections.emptySet();
    }

    @Override
    public PsiReference getReference() {
        return !isDeclaredVariable() ? new ODTVariableReference(element) : null;
    }
}
