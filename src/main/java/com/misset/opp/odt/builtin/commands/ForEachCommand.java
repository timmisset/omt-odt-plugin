package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.omt.commands.LocalVariable;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ForEachCommand extends AbstractBuiltInCommand implements CommandVariableTypeProvider {

    private static final List<String> PARAMETER_NAMES = List.of("collection", "command");
    private static final String FOR_EACH_VARIABLE = "ForEach variable";

    private ForEachCommand() {
    }

    public static final ForEachCommand INSTANCE = new ForEachCommand();

    @Override
    public String getName() {
        return "FOREACH";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return Collections.emptySet();
    }

    public List<Variable> getLocalVariables(PsiCall call, int argumentIndex) {
        if (argumentIndex != 1) {
            return Collections.emptyList();
        }
        return List.of(
                new LocalVariable("$value", "iterator value", call.resolveSignatureArgument(0), FOR_EACH_VARIABLE),
                new LocalVariable("$index", "iterator index", Set.of(OntologyModelConstants.getXsdIntegerInstance()), FOR_EACH_VARIABLE),
                new LocalVariable("$array", "all input values", call.resolveSignatureArgument(0), FOR_EACH_VARIABLE)
        );
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
