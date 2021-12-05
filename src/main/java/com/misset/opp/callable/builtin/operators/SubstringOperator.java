package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class SubstringOperator extends BuiltInStringOperator {
    private SubstringOperator() { }
    public static final SubstringOperator INSTANCE = new SubstringOperator();

    @Override
    public String getName() {
        return "SUBSTRING";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.getCallInputType(), holder, call);
        validateAllArguments(call, holder, this::validateIntegerArgument);
    }
}
