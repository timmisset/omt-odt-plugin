package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class HasOperator extends BuiltInBooleanOperator {
    private HasOperator() { }
    public static final HasOperator INSTANCE = new HasOperator();

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
}
