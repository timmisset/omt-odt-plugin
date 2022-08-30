package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CatchOperator extends AbstractBuiltInOperator {
    private CatchOperator() {
    }

    public static final CatchOperator INSTANCE = new CatchOperator();
    private static final List<String> PARAMETER_NAMES = List.of("whenError");

    @Override
    public String getName() {
        return "CATCH";
    }

    /**
     * The CATCH operator will either return the input resources or, when an error occurs, the provided
     * value in argument0.
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
    public Set<OntResource> resolveError(Set<OntResource> resources,
                                         PsiCall call) {
        return call.resolveSignatureArgument(0);
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        if (call.getNumberOfArguments() == 1) {
            Pair<Set<OntResource>, Set<OntResource>> possibilities = Pair.create(call.resolvePreviousStep(), call.resolveSignatureArgument(0));
            validateCompatibleOutcomePossibilities(possibilities, call, holder);
        }
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return call.resolvePreviousStep();
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
