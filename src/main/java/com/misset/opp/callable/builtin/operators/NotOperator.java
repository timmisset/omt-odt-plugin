package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class NotOperator extends BuiltInBooleanOperator {
    private NotOperator() { }
    public static final NotOperator INSTANCE = new NotOperator();

    @Override
    public String getName() {
        return "NOT";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateBoolean(call.getCallInputType(), holder, call);
        validateBooleanArgument(0, call, holder);
    }
}
