package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;

import java.util.List;

public class OrderByOperator extends BuiltInCollectionOperator {
    private OrderByOperator() {
    }

    public static final OrderByOperator INSTANCE = new OrderByOperator();
    private static final List<String> PARAMETER_NAMES = List.of("by", "reverse");

    @Override
    public String getName() {
        return "ORDER_BY";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateBooleanArgument(1, call, holder);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
