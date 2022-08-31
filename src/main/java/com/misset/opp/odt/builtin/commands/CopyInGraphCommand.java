package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class CopyInGraphCommand extends AbstractBuiltInCommand {

    private static final List<String> PARAMETER_NAMES = List.of("subject", "graph", "recursive");

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
        ArgumentValidator.validateInstancesArgument(0, call, holder);
        ArgumentValidator.validateNamedGraphArgument(1, call, holder);
        ArgumentValidator.validateBooleanArgument(2, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 1) {
            return Set.of(OppModelConstants.getNamedGraph());
        } else if (index == 2) {
            return Set.of(OppModelConstants.getXsdBooleanInstance());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
