package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class HasOperator extends BuiltInBooleanOperator {
    private HasOperator() { }
    public static final HasOperator INSTANCE = new HasOperator();
    private static final List<String> PARAMETER_NAMES = List.of("set", "subset");

    @Override
    public String getName() {
        return "HAS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public List<String> getFlags() {
        return IGNORE_CASE_FLAG;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntResource> ontResources = validateLeftRightCompatible(call, holder);
        validateIgnoreCaseFlagIsUsedOnStrings(ontResources, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return call.resolvePreviousStep();
        } else if (index == 1) {
            return call.resolveSignatureArgument(0);
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
