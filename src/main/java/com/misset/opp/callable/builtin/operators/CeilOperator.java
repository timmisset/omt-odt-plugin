package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class CeilOperator extends BuiltInIntegerOperator {
    private CeilOperator() { }
    public static final CeilOperator INSTANCE = new CeilOperator();

    @Override
    public String getName() {
        return "CEIL";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateDecimal(call.resolveCallInput(), holder, call);
    }
}
