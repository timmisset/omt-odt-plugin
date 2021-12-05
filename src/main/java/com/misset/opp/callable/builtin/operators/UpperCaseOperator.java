package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class UpperCaseOperator extends BuiltInStringOperator {
    private UpperCaseOperator() { }
    public static final UpperCaseOperator INSTANCE = new UpperCaseOperator();

    @Override
    public String getName() {
        return "UPPERCASE";
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
