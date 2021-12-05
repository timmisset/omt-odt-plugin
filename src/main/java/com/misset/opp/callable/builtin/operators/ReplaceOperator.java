package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class ReplaceOperator extends BuiltInStringOperator {
    private ReplaceOperator() { }
    public static final ReplaceOperator INSTANCE = new ReplaceOperator();

    @Override
    public String getName() {
        return "REPLACE";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.getCallInputType(), holder, call);
        validateAllArguments(call, holder, this::validateStringArgument);
    }
}
