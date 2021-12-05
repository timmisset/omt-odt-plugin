package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class TruncOperator extends BuiltInCollectionOperator {
    private TruncOperator() { }
    public static final TruncOperator INSTANCE = new TruncOperator();

    @Override
    public String getName() {
        return "TRUNC";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateDateTime(call.getCallInputType(), holder, call);
        validateStringArgument(0, call, holder);

    }
}
