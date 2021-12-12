package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class TrimOperator extends BuiltInStringOperator {
    private TrimOperator() { }
    public static final TrimOperator INSTANCE = new TrimOperator();

    @Override
    public String getName() {
        return "TRIM";
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
