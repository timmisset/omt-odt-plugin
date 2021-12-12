package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class LengthOperator extends BuiltInIntegerOperator {
    private LengthOperator() { }
    public static final LengthOperator INSTANCE = new LengthOperator();

    @Override
    public String getName() {
        return "LENGTH";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.resolveCallInput(), holder, call);
    }
}
