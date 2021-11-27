package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class CopyInGraphCommand extends BuiltInCommand {
    private CopyInGraphCommand() {
    }

    public static final CopyInGraphCommand INSTANCE = new CopyInGraphCommand();

    @Override
    public String getName() {
        return "COPY_IN_GRAPH";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    protected Set<OntResource> resolveFrom(Call call) {
        return call.resolveSignatureArgument(0);
    }

    @Override
    public void specificValidation(PsiCall call,
                                   ProblemsHolder holder) {
        validateInstancesArgument(0, call, holder);
        validateNamedGraphArgument(1, call, holder);
        validateBooleanArgument(2, call, holder);
    }
}
