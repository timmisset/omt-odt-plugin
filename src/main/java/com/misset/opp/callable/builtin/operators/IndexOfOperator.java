package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class IndexOfOperator extends BuiltInIntegerOperator {
    private IndexOfOperator() { }
    public static final IndexOfOperator INSTANCE = new IndexOfOperator();

    @Override
    public String getName() {
        return "INDEX_OF";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.getCallInputType(), holder, call);
        validateStringArgument(0, call, holder);
    }
}
