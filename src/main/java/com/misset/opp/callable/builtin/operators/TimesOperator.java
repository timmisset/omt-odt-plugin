package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class TimesOperator extends BuiltinMathOperator {
    private TimesOperator() { }
    public static final TimesOperator INSTANCE = new TimesOperator();

    @Override
    public String getName() {
        return "TIMES";
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
            TTLValidationUtil.validateNumber(call.getCallInputType(), holder, call);
        }
        validateAllArguments(call, holder, this::validateNumberArgument);
    }
}
