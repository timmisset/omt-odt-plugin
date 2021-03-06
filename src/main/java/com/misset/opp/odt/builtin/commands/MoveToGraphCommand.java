package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class MoveToGraphCommand extends BuiltInCommand {
    private MoveToGraphCommand() { }
    public static final MoveToGraphCommand INSTANCE = new MoveToGraphCommand();

    @Override
    public String getName() {
        return "MOVE_TO_GRAPH";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return call.resolveSignatureArgument(0);
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        validateInstancesArgument(0, call, holder);
        validateNamedGraphArgument(1, call, holder);
    }


    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 1) {
            return Set.of(OppModelConstants.NAMED_GRAPH);
        }
        return null;
    }
}
