package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;

public class PickOperator extends BuiltInCollectionOperator {
    private PickOperator() { }
    public static final PickOperator INSTANCE = new PickOperator();

    @Override
    protected String getShorthandSyntax() {
        return "[]";
    }

    @Override
    public String getName() {
        return "PICK";
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
        validateIntegerArgument(0, call, holder);
    }
}
