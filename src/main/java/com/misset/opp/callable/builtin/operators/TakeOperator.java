package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;

public class TakeOperator extends BuiltInCollectionOperator {
    private TakeOperator() { }
    public static final TakeOperator INSTANCE = new TakeOperator();

    @Override
    public String getName() {
        return "TAKE";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateIntegerArgument(0, call, holder);
    }
}
