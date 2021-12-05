package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class LowerCaseOperator extends BuiltInStringOperator {
    private LowerCaseOperator() { }
    public static final LowerCaseOperator INSTANCE = new LowerCaseOperator();

    @Override
    public String getName() {
        return "LOWERCASE";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.getCallInputType(), holder, call);
    }
}
