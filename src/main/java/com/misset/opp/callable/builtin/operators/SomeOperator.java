package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class SomeOperator extends BuiltInBooleanOperator {
    private SomeOperator() { }
    public static final SomeOperator INSTANCE = new SomeOperator();

    @Override
    public String getName() {
        return "SOME";
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
        if(call.numberOfArguments() == 0) {
            TTLValidationUtil.validateBoolean(call.getCallInputType(), holder, call);
        } else {
            validateBooleanArgument(0, call, holder);
        }
    }
}
