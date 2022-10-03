package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.builtin.commands.CommandVariableTypeProvider;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.PsiRelationshipUtil;
import com.misset.opp.odt.psi.reference.ODTVariableReference;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.Variable;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ODTUsageVariableDelegate extends ODTVariableDelegateAbstract {

    public ODTUsageVariableDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public boolean isDeclaredVariable() {
        return false;
    }

    @Override
    @Nullable
    public PsiReference getReference() {
        if (isDeclaredVariable()) {
            return null;
        }
        return ODTVariableReference.forVariable(element);
    }

    /**
     * A variable can be typed by many parts of the OMT and ODT languages
     * - via a PsiElement reference result
     * - local variable (either from OMT entry or ODT procedure/command) with context depending type
     * - global variable with static type
     */
    @Override
    public Set<OntResource> resolve() {
        return resolveFromDeclaringVariable()
                .or(() -> getFromFile().map(Resolvable::resolve))
                .or(this::resolveFromCallContext)
                .orElse(resolveFromContext());
    }

    private Optional<Variable> getUndeclaredVariable() {
        return getDeclaringVariable().or(this::getFromFile);
    }

    private Set<OntResource> resolveFromContext() {
        // combine the type assignments up to this point
        // and potentially set by the declaration (OMT or ODT)
        final HashSet<OntResource> resources = new HashSet<>();
        resources.addAll(getTypeFromAssignment().orElse(Collections.emptySet()));
        resources.addAll(getTypeFromDeclaration().orElse(Collections.emptySet()));
        return resources;
    }

    private Optional<Set<OntResource>> getTypeFromAssignment() {
        final List<ODTVariable> variablesFromAssignments = getVariablesFromAssignments();
        if (variablesFromAssignments.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(variablesFromAssignments.stream()
                .map(ODTVariableDelegate::resolve)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
    }

    private List<ODTVariable> getVariablesFromAssignments() {
        // find the closest variable assignment to determine earlier instances or assignments
        // of this variable to determine the type
        // the 'other' variable must resolve to the same declared variable as this instance
        List<PsiElement> relatedElements = PsiRelationshipUtil.getRelatedElements(getElement());
        return relatedElements.stream()
                .filter(ODTVariable.class::isInstance)
                .map(ODTVariable.class::cast)
                .filter(ODTVariableDelegate::isAssignedVariable)
                .collect(Collectors.toList());
    }

    @Override
    public Variable getDeclared() {
        PsiFile containingFile = getElement().getContainingFile();
        if (!(containingFile instanceof ODTFile)) {
            return null;
        }

        return getDeclaredFromReference()
                .or(this::getUndeclaredVariable)
                .or(this::getDeclaredFromCallContext)
                .orElse(null);
    }

    private Optional<Variable> getDeclaredFromReference() {
        return Optional.ofNullable(getReference())
                .filter(ODTVariableReference.class::isInstance)
                .map(ODTVariableReference.class::cast)
                .map(odtVariableReference -> odtVariableReference.resolve(false, false))
                .filter(Variable.class::isInstance)
                .map(Variable.class::cast);
    }

    private Optional<Set<OntResource>> getTypeFromDeclaration() {
        return Optional.ofNullable(getDeclared())
                .map(Variable::resolve);
    }

    private Optional<Set<OntResource>> resolveFromDeclaringVariable() {
        return getDeclaringVariable().map(Variable::resolve);
    }

    private Optional<Variable> getDeclaringVariable() {
        PsiFile containingFile = element.getContainingFile();
        if (!(containingFile instanceof ODTFile)) {
            return Optional.empty();
        }
        Collection<Variable> variables = ((ODTFile) containingFile).getVariables(element.getName());
        if (variables.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(variables.iterator().next());
        }
    }

    private Optional<Set<OntResource>> resolveFromCallContext() {
        return getCallableLocalVariableTypeProvider()
                .map(pair -> pair.second.resolve(element.getName(), pair.first, pair.first.getArgumentIndexOf(element)))
                .filter(ontResources -> !ontResources.isEmpty());
    }

    private Optional<Variable> getDeclaredFromCallContext() {
        return getCallableLocalVariableTypeProvider()
                .map(pair -> pair.second.getLocalVariable(element.getName(), pair.first, pair.first.getArgumentIndexOf(element)));
    }

    private Optional<Pair<ODTCall, CommandVariableTypeProvider>> getCallableLocalVariableTypeProvider() {
        return PsiTreeUtil.collectParents(element, ODTCall.class, false, Objects::isNull)
                .stream()
                .map(call -> new Pair<>(call, call.getCallable()))
                .filter(pair -> pair.second instanceof CommandVariableTypeProvider)
                .map(pair -> new Pair<>(pair.first, (CommandVariableTypeProvider) pair.second))
                .findFirst();
    }

    @Override
    public List<Literal> resolveLiteral() {
        List<ODTVariable> variablesFromAssignments = getVariablesFromAssignments();
        if (variablesFromAssignments.isEmpty()) {
            return Collections.emptyList();
        }
        ODTVariable variable = variablesFromAssignments.get(0);
        return variable.resolveLiteral();
    }
}
