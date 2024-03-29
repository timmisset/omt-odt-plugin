package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MergeOperator extends AbstractBuiltInOperator {
    private MergeOperator() {
    }

    public static final MergeOperator INSTANCE = new MergeOperator();
    private static final List<String> PARAMETER_NAMES = List.of("collection", "collection");

    @Override
    public String getName() {
        return "MERGE";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           PsiCall call) {
        final int numberOfArguments = call.getNumberOfArguments();
        final HashSet<OntResource> output = new HashSet<>();
        if (numberOfArguments == 1) {
            // combines the input + argument:
            output.addAll(resources);
            output.addAll(call.resolveSignatureArgument(0));
        } else if (numberOfArguments > 1) {
            // combines the arguments only
            for (int i = 0; i < numberOfArguments; i++) {
                output.addAll(call.resolveSignatureArgument(i));
            }
        }
        return output;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        ArgumentValidator.validateAllArgumentsCompatible(call, holder);
        if (call.getNumberOfArguments() >= 2 && !call.resolvePreviousStep().isEmpty()) {
            holder.registerProblem(call, "Using 2 or more arguments will ignore the input value, consider using pipes instead (<query> | <query> | <query>)", ProblemHighlightType.WARNING);
        }
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
