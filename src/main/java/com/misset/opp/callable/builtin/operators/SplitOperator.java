package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class SplitOperator extends BuiltInStringOperator {
    private SplitOperator() { }
    public static final SplitOperator INSTANCE = new SplitOperator();

    @Override
    public String getName() {
        return "SPLIT";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.getCallInputType(), holder, call);
        validateStringArgument(0, call, holder);
    }
}
