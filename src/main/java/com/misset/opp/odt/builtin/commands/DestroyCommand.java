package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class DestroyCommand extends BuiltInCommand {

    private static final List<String> PARAMETER_NAMES = List.of("subject");

    private DestroyCommand() {
    }

    public static final DestroyCommand INSTANCE = new DestroyCommand();

    @Override
    public String getName() {
        return "DESTROY";
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return Collections.emptySet();
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        validateInstancesArgument(0, call, holder);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
