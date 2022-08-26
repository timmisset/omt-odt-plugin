package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IfEmptyOperator extends BuiltInOperator {
    private IfEmptyOperator() { }

    public static final IfEmptyOperator INSTANCE = new IfEmptyOperator();
    private static final List<String> PARAMETER_NAMES = List.of("alternative");

    @Override
    public String getName() {
        return "IF_EMPTY";
    }

    /**
     * The IF_EMPTY operator will either return the input resources or, when empty, return the provided value
     * At compilation both types are possible
     */
    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        PsiCall call) {
        final HashSet<OntResource> ontResources = new HashSet<>(resources);
        ontResources.addAll(call.resolveSignatureArgument(0));
        return ontResources;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        if (call.getNumberOfArguments() == 1) {
            Pair<Set<OntResource>, Set<OntResource>> possibilities = Pair.create(call.resolvePreviousStep(), call.resolveSignatureArgument(0));
            validateCompatibleOutcomePossibilities(possibilities, call, holder);
        }
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return call.resolvePreviousStep();
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
