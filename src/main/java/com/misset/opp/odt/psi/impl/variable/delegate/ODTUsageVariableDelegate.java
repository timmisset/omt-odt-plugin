package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.callable.Variable;
import com.misset.opp.callable.global.GlobalVariable;
import com.misset.opp.callable.local.LocalVariableTypeProvider;
import com.misset.opp.callable.psi.PsiVariable;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.odt.psi.reference.ODTVariableReference;
import com.misset.opp.omt.meta.providers.OMTLocalVariableProvider;
import com.misset.opp.omt.psi.impl.variable.OMTVariableImpl;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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
        return new ODTVariableReference(element);
    }

    /**
     * A variable can be typed by many parts of the OMT and ODT languages
     * - via a PsiElement reference result
     * - local variable (either from OMT entry or ODT procedure/command) with context depending type
     * - global variable with static type
     */
    @Override
    public Set<OntResource> getType() {
        return getTypeFromOMTLocalVariable()
                .or(this::getTypeFromGlobalVariable)
                .or(this::getTypeFromCallContext)
                .orElse(getTypeFromContext());
    }

    private Set<OntResource> getTypeFromContext() {
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
                .map(ODTVariableDelegate::getType)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet()));
    }

    private List<ODTVariable> getVariablesFromAssignments(@Nullable ODTScriptLine scriptLine,
                                                          PsiElement target) {
        List<ODTVariable> assignmentVariables = new ArrayList<>();
        if (scriptLine == null) {
            return assignmentVariables;
        }
        ODTScript script = PsiTreeUtil.getParentOfType(scriptLine, ODTScript.class);
        if (script == null) {
            return assignmentVariables;
        }

        while (scriptLine != null) {
            assignmentVariables.addAll(PsiTreeUtil.findChildrenOfType(scriptLine, ODTVariableAssignment.class)
                    .stream()
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
        final ODTScriptLine precedingScriptline = Optional.ofNullable(parentScriptline)
                .map(parent -> PsiTreeUtil.getPrevSiblingOfType(parent, ODTScriptLine.class))
                .orElse(null);
        assignmentVariables.addAll(getVariablesFromAssignments(precedingScriptline, target));
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
        return Optional.ofNullable(variable.getReference())
                .map(PsiReference::resolve)
                .map(this::getWrapper)
                .map(target::equals)
                .orElse(false);
    }

    private Optional<Set<OntResource>> getTypeFromDeclaration() {
        return Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .map(this::getWrapper)
                .map(Variable::getType);
    }

    private PsiVariable getWrapper(PsiElement element) {
        if (element instanceof PsiVariable) {
            return (PsiVariable) element;
        }
        return element.getUserData(OMTVariableImpl.WRAPPER);
    }

    private Optional<Set<OntResource>> getTypeFromOMTLocalVariable() {
        return element.getContainingFile()
                .getProviders(YAMLValue.class, OMTLocalVariableProvider.class)
                .entrySet()
                .stream()
                // map the Provider and YamlPsiElement (mapping) to resolve to set of local variables that are present
                .map(entry -> entry.getValue().getLocalVariableMap(entry.getKey()).get(element.getName()))
                .filter(Objects::nonNull)
                // resolve the type from the local variable
                // the type is set by OMTLocalVariableTypeProvider corresponding with the OMTLocalVariableProvider
                .map(Variable::getType)
                .findFirst();
    }

    private Optional<Set<OntResource>> getTypeFromGlobalVariable() {
        return Optional.ofNullable(GlobalVariable.getVariable(element.getName()))
                .map(Variable::getType);
    }

    private Optional<Set<OntResource>> getTypeFromCallContext() {
        return PsiTreeUtil.collectParents(element, ODTCall.class, false, Objects::isNull)
                .stream()
                .map(call -> new Pair<>(call, call.getCallable()))
                .filter(pair -> pair.second instanceof LocalVariableTypeProvider)
                .map(pair -> ((LocalVariableTypeProvider) pair.second).getType(element.getName(), pair.first))
                .findFirst();
    }
}
