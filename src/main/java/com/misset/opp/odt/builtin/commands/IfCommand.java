package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class IfCommand extends BuiltInCommand {
    private IfCommand() { }
    public static final IfCommand INSTANCE = new IfCommand();

    @Override
    public String getName() {
        return "IF";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        final HashSet<OntResource> resources = new HashSet<>();
        resources.addAll(call.resolveSignatureArgument(1));
        resources.addAll(call.resolveSignatureArgument(2));
        return resources;
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        validateBooleanArgument(0, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
        }
        return null;
    }
}
