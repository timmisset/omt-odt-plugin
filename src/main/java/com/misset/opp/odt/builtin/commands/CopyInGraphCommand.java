package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

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
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return call.resolveSignatureArgument(0);
    }

    @Override
    public void specificValidation(PsiCall call,
                                   ProblemsHolder holder) {
        validateInstancesArgument(0, call, holder);
        validateNamedGraphArgument(1, call, holder);
        validateBooleanArgument(2, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 1) {
            return Set.of(OppModel.INSTANCE.NAMED_GRAPH);
        } else if (index == 2) {
            return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
        }
        return null;
    }
}
