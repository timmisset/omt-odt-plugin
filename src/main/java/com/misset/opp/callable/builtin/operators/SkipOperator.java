package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;

public class SkipOperator extends BuiltInCollectionOperator {
    private SkipOperator() { }
    public static final SkipOperator INSTANCE = new SkipOperator();

    @Override
    public String getName() {
        return "SKIP";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateIntegerArgument(0, call, holder);
    }
}
