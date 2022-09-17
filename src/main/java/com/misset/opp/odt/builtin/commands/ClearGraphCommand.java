package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class ClearGraphCommand extends AbstractBuiltInCommand {

    private static final List<String> PARAMETER_NAMES = List.of("graph");

    private ClearGraphCommand() {
    }

    public static final ClearGraphCommand INSTANCE = new ClearGraphCommand();

    @Override
    public String getName() {
        return "CLEAR_GRAPH";
    }

    @Override
    public OntResource resolveSingle() {
        return OntologyModelConstants.getXsdIntegerInstance();
    }

    @Override
    public void specificValidation(PsiCall call,
                                   ProblemsHolder holder) {
        ArgumentValidator.validateNamedGraphArgument(0, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OntologyModelConstants.getNamedGraph());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
