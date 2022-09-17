package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;

public class NewGraphCommand extends AbstractBuiltInCommand {

    private static final List<String> PARAMETER_NAMES = List.of("graph");

    private NewGraphCommand() {
    }

    public static final NewGraphCommand INSTANCE = new NewGraphCommand();

    @Override
    public String getName() {
        return "NEW_GRAPH";
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public OntResource resolveSingle() {
        return OntologyModelConstants.getNamedGraph();
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        ArgumentValidator.validateGraphShapeArgument(0, call, holder);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }

    @Override
    public boolean callCompletionOnInsert() {
        return true;
    }
}
