package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

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
        return OppModel.INSTANCE.XSD_INTEGER_INSTANCE;
    }

    @Override
    public void specificValidation(PsiCall call,
                                   ProblemsHolder holder) {
        validateNamedGraphArgument(0, call, holder);
    }
}
