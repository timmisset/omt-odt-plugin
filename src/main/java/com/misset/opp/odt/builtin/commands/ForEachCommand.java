package com.misset.opp.odt.builtin.commands;

import com.misset.opp.resolvable.local.LocalVariable;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.shared.providers.CallableLocalVariableTypeProvider;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ForEachCommand extends BuiltInCommand implements CallableLocalVariableTypeProvider {

    private static final List<String> PARAMETER_NAMES = List.of("collection", "command");

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
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return Collections.emptySet();
    }

    public List<LocalVariable> getLocalVariables(PsiCall call, int argumentIndex) {
        if (argumentIndex != 1) {
            return Collections.emptyList();
        }
        return List.of(
                new LocalVariable("$value", "iterator value", call.resolveSignatureArgument(0)),
                new LocalVariable("$index", "iterator index", Set.of(OppModelConstants.XSD_INTEGER_INSTANCE)),
                new LocalVariable("$array", "all input values", call.resolveSignatureArgument(0))
        );
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
