package com.misset.opp.odt.builtin.commands;

import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.local.LocalVariable;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.shared.providers.CallableLocalVariableTypeProvider;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MapCommand extends AbstractBuiltInCommand implements CallableLocalVariableTypeProvider {

    private static final List<String> PARAMETER_NAMES = List.of("collection", "command");
    private static final String MAP_VARIABLE = "Map variable";

    private MapCommand() {
    }

    public static final MapCommand INSTANCE = new MapCommand();

    @Override
    public String getName() {
        return "MAP";
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
    public List<Variable> getLocalVariables(PsiCall call, int argumentIndex) {
        if (argumentIndex != 1) {
            return Collections.emptyList();
        }
        return List.of(
                new LocalVariable("$value", "iterator value", call.resolveSignatureArgument(0), MAP_VARIABLE),
                new LocalVariable("$index", "iterator index", call.resolveSignatureArgument(0), MAP_VARIABLE),
                new LocalVariable("$array", "all input values", call.resolveSignatureArgument(0), MAP_VARIABLE)
        );
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
