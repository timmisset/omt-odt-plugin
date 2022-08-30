package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IIfOperator extends AbstractBuiltInOperator {
    private IIfOperator() {
    }

    public static final IIfOperator INSTANCE = new IIfOperator();
    private static final List<String> PARAMETER_NAMES = List.of("condition", "then", "else");

    @Override
    public String getName() {
        return "IIF";
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public Set<OntResource> resolveFrom(Set<OntResource> resources,
                                        PsiCall call) {
        final HashSet<OntResource> output = new HashSet<>();
        output.addAll(call.resolveSignatureArgument(1));
        output.addAll(call.resolveSignatureArgument(2));
        return output;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        ArgumentValidator.validateBooleanArgument(0, call, holder);
        if (call.getNumberOfArguments() == 3) {
            Pair<Set<OntResource>, Set<OntResource>> possibilities = Pair.create(call.resolveSignatureArgument(1), call.resolveSignatureArgument(2));
            validateCompatibleOutcomePossibilities(possibilities, call, holder);
        }
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
        } else if (index == 1) {
            return call.resolveSignatureArgument(2);
        } else if (index == 2) {
            return call.resolveSignatureArgument(1);
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
