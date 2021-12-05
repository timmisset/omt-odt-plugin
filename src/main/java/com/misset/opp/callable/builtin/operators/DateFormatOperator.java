package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class DateFormatOperator extends BuiltInStringOperator {
    private DateFormatOperator() { }
    public static final DateFormatOperator INSTANCE = new DateFormatOperator();

    @Override
    public String getName() {
        // that's correct, the classname and function call in this ODT operator are different
        return "FORMAT_DATE";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateDateTime(call.getCallInputType(), holder, call);
        validateStringArgument(0, call, holder);
    }
}
