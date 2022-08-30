package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;

import java.util.List;

public class CountOperator extends BuiltInIntegerOperator {
    private CountOperator() {
    }

    public static final CountOperator INSTANCE = new CountOperator();
    private static final List<String> PARAMETER_NAMES = List.of("include");

    @Override
    public String getName() {
        return "COUNT";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        ArgumentValidator.validateBooleanArgument(0, call, holder);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
