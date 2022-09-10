package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiVariable;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.builtin.commands.CommandVariableTypeProvider;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.reference.ODTVariableReference;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.Variable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ODTUsageVariableDelegate extends ODTBaseVariableDelegate {

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
        // find the closest variable assignment to determine earlier instances or assignments
        // of this variable to determine the type
        // the 'other' variable must resolve to the same declared variable as this instance
        final PsiElement resolve = Optional.ofNullable(getReference())
                .filter(ODTVariableReference.class::isInstance)
                .map(ODTVariableReference.class::cast)
                .map(odtVariableReference -> odtVariableReference.resolve(false, false))
                .filter(PsiVariable.class::isInstance)
                .map(PsiVariable.class::cast)
                .orElse(null);

        if (resolve == null) {
            return Optional.empty();
        }

        final List<ODTVariable> variablesFromAssignments = getVariablesFromAssignments(PsiTreeUtil.getParentOfType(
                element,
                ODTScriptLine.class), resolve);
        if (variablesFromAssignments.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(variablesFromAssignments.stream()
                .map(ODTVariableDelegate::resolve)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
    }

    public List<ODTVariable> getVariablesFromAssignments(@Nullable ODTScriptLine scriptLine,
                                                         PsiElement target) {
        List<ODTVariable> assignmentVariables = new ArrayList<>();
        if (scriptLine == null) {
            return assignmentVariables;
        }
        ODTScript script = PsiTreeUtil.getParentOfType(scriptLine, ODTScript.class);
        if (script == null) {
            return assignmentVariables;
        }

        // no self-referencing
        // for example:
        // VAR $total = $total / PLUS(1)
        // the value $total should not be resolved by resolving the assignment
        scriptLine = PsiTreeUtil.getPrevSiblingOfType(scriptLine, ODTScriptLine.class);

        while (scriptLine != null) {
            assignmentVariables.addAll(PsiTreeUtil.findChildrenOfType(scriptLine, ODTVariable.class)
                    .stream()
                    .filter(variable ->
                            variable != element &&
                                    variable.getParent() instanceof ODTVariableAssignment &&
                                    variable.getName() != null && variable.getName().equals(element.getName()))
                    .map(variable -> (ODTVariableAssignment) variable.getParent())
                    .map(assignment -> getVariableFromAssignment(assignment, target))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList()));
            scriptLine = PsiTreeUtil.getPrevSiblingOfType(scriptLine, ODTScriptLine.class);
        }

        final ODTScriptLine parentScriptline = PsiTreeUtil.getParentOfType(script, ODTScriptLine.class);
        assignmentVariables.addAll(getVariablesFromAssignments(parentScriptline, target));
        return assignmentVariables;
    }

    private ODTVariable getVariableFromAssignment(ODTVariableAssignment variableAssignment,
                                                  PsiElement target) {
        return variableAssignment.getVariableList().stream()
                .filter(variable -> hasSameTarget(variable, target))
                .findFirst()
                .orElse(null);
    }

    private boolean hasSameTarget(ODTVariable variable,
                                  PsiElement target) {
        return variable == target || Optional.ofNullable(variable.getReference())
                .filter(ODTVariableReference.class::isInstance)
                .map(ODTVariableReference.class::cast)
                .map(odtVariableReference -> odtVariableReference.resolve(false, false))
                .filter(Variable.class::isInstance)
                .map(Variable.class::cast)
                .map(target::equals)
                .orElse(false);
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
}
