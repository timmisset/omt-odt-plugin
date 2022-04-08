package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class EqualsOperator extends BuiltInBooleanOperator {
    private EqualsOperator() { }
    public static final EqualsOperator INSTANCE = new EqualsOperator();

    @Override
    public String getName() {
        return "EQUALS";
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
        validateIgnoreCaseFlagUsage(1, call, holder);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return call.resolvePreviousStep();
        } else if (index == 1) {
            return call.resolveSignatureArgument(0);
        }
        return null;
    }
}
