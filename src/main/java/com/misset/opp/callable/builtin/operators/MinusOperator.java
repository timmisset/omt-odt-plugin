package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class MinusOperator extends BuiltinMathOperator {
    private MinusOperator() { }
    public static final MinusOperator INSTANCE = new MinusOperator();

    @Override
    public String getName() {
        return "MINUS";
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
        if(call.numberOfArguments() == 1) {
            TTLValidationUtil.validateRequiredTypes(validInputs, call.getCallInputType(), holder, call);
        }
        validateAllArguments(call, holder, validator);
    }
}
