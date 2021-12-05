package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public class MergeOperator extends BuiltInOperator {
    private MergeOperator() { }
    public static final MergeOperator INSTANCE = new MergeOperator();

    @Override
    public String getName() {
        return "MERGE";
    }

    @Override
    protected String getShorthandSyntax() {
        return "|";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           Call call) {
        final int numberOfArguments = call.numberOfArguments();
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
        validateAllArgumentsCompatible(call, holder);
        if(call.numberOfArguments() >= 2 && !call.getCallInputType().isEmpty()) {
            holder.registerProblem(call, "Using 2 or more arguments will ignore the input value, consider using pipes instead (<query> | <query> | <query>)", ProblemHighlightType.WARNING);
        }
    }
}
