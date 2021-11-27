package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.Set;

public class DestroyCommand extends BuiltInCommand {
    private DestroyCommand() { }
    public static final DestroyCommand INSTANCE = new DestroyCommand();

    @Override
    public String getName() {
        return "DESTROY";
    }

    @Override
    protected Set<OntResource> resolveFrom(Call call) {
        return Collections.emptySet();
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        validateInstancesArgument(0, call, holder);
    }
}
