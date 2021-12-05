package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class AndOperator extends BuiltInBooleanOperator {
    private AndOperator() { }
    public static final AndOperator INSTANCE = new AndOperator();

    @Override
    public String getName() {
        return "AND";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        if(call.numberOfArguments() == 1) {
            TTLValidationUtil.validateBoolean(call.getCallInputType(), holder, call);
        }
        validateAllArguments(call, holder, this::validateBooleanArgument);
    }
}
