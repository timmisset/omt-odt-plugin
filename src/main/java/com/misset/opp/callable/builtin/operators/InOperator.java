package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class InOperator extends BuiltInBooleanOperator {
    private InOperator() { }
    public static final InOperator INSTANCE = new InOperator();

    @Override
    public String getName() {
        return "IN";
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
}
