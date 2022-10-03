package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.reference.ODTVariableReference;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.Variable;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;

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
                .filter(ODTVariableReference.class::isInstance)
                .map(ODTVariableReference.class::cast)
                .map(odtVariableReference -> odtVariableReference.resolve(false, false))
                .filter(Variable.class::isInstance)
                .map(Variable.class::cast)
                .or(this::getFromFile)
                .orElse(null);
    }

    @Override
    public boolean isAssignedVariable() {
        return !isAssignmentPart();
    }

    @Override
    public Set<OntResource> resolve() {
        int index = getVariableIndex();
        Resolvable resolvable = getResolvableValue();
        if (resolvable != null) {
            if (index == 0) {
                return resolvable.resolve();
            }
            if (index == 1 && resolvable instanceof Callable) {
                return ((Callable) resolvable).getSecondReturnArgument();
            }
        }
        return Collections.emptySet();
    }

    @Override
    public PsiReference getReference() {
        return !isDeclaredVariable() ? ODTVariableReference.forVariable(element) : null;
    }

    @Override
    public List<Literal> resolveLiteral() {
        return Optional.ofNullable(getResolvableValue())
                .map(Resolvable::resolveLiteral)
                .orElse(Collections.emptyList());
    }

    private Resolvable getResolvableValue() {
        final ODTVariableAssignment variableAssignment = PsiTreeUtil.getParentOfType(element,
                ODTVariableAssignment.class);
        if (variableAssignment == null) {
            return null;
        }

        final int i = getVariableIndex();
        final ODTVariableValue variableValue = variableAssignment.getVariableValue();
        if (i == 0) {
            ODTStatement statement = variableValue.getStatement();
            if (statement instanceof ODTResolvable) {
                return (Resolvable) statement;
            }
        } else if (i == 1) {
            return Optional.of(variableValue.getStatement())
                    .filter(ODTCommandCall.class::isInstance)
                    .map(ODTCommandCall.class::cast)
                    .map(ODTCall::getCallable)
                    .orElse(null);
        }
        return null;
    }

    private int getVariableIndex() {
        final ODTVariableAssignment variableAssignment = PsiTreeUtil.getParentOfType(element,
                ODTVariableAssignment.class);
        if (variableAssignment == null) {
            return -1;
        }
        final List<ODTVariable> variableList = variableAssignment.getVariableList();
        return variableList.indexOf(element);
    }
}
