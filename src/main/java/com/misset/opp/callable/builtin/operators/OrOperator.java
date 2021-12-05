package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class OrOperator extends BuiltInBooleanOperator {
    private OrOperator() { }
    public static final OrOperator INSTANCE = new OrOperator();

    @Override
    public String getName() {
        return "OR";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        if(call.numberOfArguments() == 1) {
            TTLValidationUtil.validateBoolean(call.getCallInputType(), holder, call);
        }
        validateAllArguments(call, holder, this::validateBooleanArgument);
    }
}
