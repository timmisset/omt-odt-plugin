package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class PlusOperator extends BuiltinMathOperator {
    private PlusOperator() { }
    public static final PlusOperator INSTANCE = new PlusOperator();

    @Override
    public String getName() {
        return "PLUS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        if (call.numberOfArguments() == 1) {
            TTLValidationUtil.validateRequiredTypes(validInputs, call.resolveCallInput(), holder, call);
        }
        validateAllArguments(call, holder, validator);
    }


    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return validInputs;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return validInputs;
    }
}
