package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public class IfEmptyOperator extends BuiltInOperator {
    private IfEmptyOperator() { }
    public static final IfEmptyOperator INSTANCE = new IfEmptyOperator();

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
                                        Call call) {
        final HashSet<OntResource> ontResources = new HashSet<>(resources);
        ontResources.addAll(call.resolveSignatureArgument(0));
        return ontResources;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        if(call.numberOfArguments() == 1) {
            Pair<Set<OntResource>, Set<OntResource>> possibilities = Pair.create(call.getCallInputType(), call.resolveSignatureArgument(0));
            validateCompatibleOutcomePossibilities(possibilities, call, holder);
        }
    }
}
