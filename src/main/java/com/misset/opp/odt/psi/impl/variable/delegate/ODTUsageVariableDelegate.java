package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.reference.ODTVariableReference;
import com.misset.opp.omt.meta.providers.OMTLocalVariableProvider;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.global.GlobalVariable;
import com.misset.opp.shared.providers.CallableLocalVariableTypeProvider;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLValue;

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
        return resolveFromOMTLocalVariable()
                .or(this::resolveFromGlobalVariable)
                .or(this::resolveFromCallContext)
                .orElse(resolveFromContext());
    }


    private Optional<Variable> getUndeclaredVariable() {
        return getFromOMTLocalVariable()
                .or(this::getFromGlobalVariable);
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
                .map(PsiReference::resolve)
                .map(this::getWrapper)
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
        // VAR $total = $total / PLUS(1);
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
        // check if there are parent containers that might include additional assignments to be included
        // $variable = 'a'; <-- only string at this point
        // IF true {
        //    $variable = 1;
        //    @LOG($variable); <-- can be both string and number at this point
        // } ELSE {
        //    @LOG($variable); <-- only string at this point
        // }
        // @LOG($variable); <-- and again both string at number at this point
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
                .map(PsiReference::resolve)
                .map(this::getWrapper)
                .map(target::equals)
                .orElse(false);
    }

    @Override
    public Variable getDeclared() {
        return getDeclaredFromReference()
                .or(this::getUndeclaredVariable)
                .or(this::getDeclaredFromCallContext)
                .orElse(null);
    }

    private Optional<Variable> getDeclaredFromReference() {
        return Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .map(this::getWrapper);
    }

    private Optional<Set<OntResource>> getTypeFromDeclaration() {
        return Optional.ofNullable(getDeclared())
                .map(Variable::resolve);
    }


    private Optional<Set<OntResource>> resolveFromOMTLocalVariable() {
        return getFromOMTLocalVariable().map(Variable::resolve);
    }

    private Optional<Variable> getFromOMTLocalVariable() {
        // resolve the type from the local variable
        // the type is set by OMTLocalVariableTypeProvider corresponding with the OMTLocalVariableProvider
        return element.getODTFile()
                .getProviders(YAMLValue.class, OMTLocalVariableProvider.class, OMTLocalVariableProvider.KEY)
                .entrySet()
                .stream()
                // map the Provider and YamlPsiElement (mapping) to resolve to set of local variables that are present
                .map(entry -> entry.getValue().getLocalVariableMap(entry.getKey()).get(element.getName()))
                .filter(Objects::nonNull)
                .map(Variable.class::cast)
                .findFirst();
    }

    private Optional<Set<OntResource>> resolveFromGlobalVariable() {
        return getFromGlobalVariable().map(Variable::resolve);
    }

    private Optional<Variable> getFromGlobalVariable() {
        return Optional.ofNullable(GlobalVariable.getVariable(element.getName()));
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

    private Optional<Pair<ODTCall, CallableLocalVariableTypeProvider>> getCallableLocalVariableTypeProvider() {
        return PsiTreeUtil.collectParents(element, ODTCall.class, false, Objects::isNull)
                .stream()
                .map(call -> new Pair<>(call, call.getCallable()))
                .filter(pair -> pair.second instanceof CallableLocalVariableTypeProvider)
                .map(pair -> new Pair<>(pair.first, (CallableLocalVariableTypeProvider) pair.second))
                .findFirst();
    }
}
