package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;

public class FilterOperator extends BuiltInCollectionOperator {
    // todo:
    // implement a filter mechanism just like for the filter that is part of the queryStep

    private FilterOperator() { }
    public static final FilterOperator INSTANCE = new FilterOperator();

    @Override
    public String getName() {
        return "FILTER";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateBooleanArgument(0, call, holder);
    }
}
