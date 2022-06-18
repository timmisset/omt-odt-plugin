package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.List;

public class NewGraphCommand extends BuiltInCommand {

    private static final List<String> PARAMETER_NAMES = List.of("graph");

    private NewGraphCommand() {
    }

    public static final NewGraphCommand INSTANCE = new NewGraphCommand();

    @Override
    public String getName() {
        return "NEW_GRAPH";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModelConstants.NAMED_GRAPH;
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        validateGraphShapeArgument(0, call, holder);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
