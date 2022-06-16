package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ClearGraphCommand extends BuiltInCommand {
    private ClearGraphCommand() {
    }

    public static final ClearGraphCommand INSTANCE = new ClearGraphCommand();

    @Override
    public String getName() {
        return "CLEAR_GRAPH";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.XSD_INTEGER_INSTANCE;
    }

    @Override
    public void specificValidation(PsiCall call,
                                   ProblemsHolder holder) {
        validateNamedGraphArgument(0, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.NAMED_GRAPH);
        }
        return null;
    }
}
