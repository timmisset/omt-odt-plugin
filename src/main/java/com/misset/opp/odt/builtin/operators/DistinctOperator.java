package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;

import java.util.List;

public class DistinctOperator extends BuiltInCollectionOperator {
    private DistinctOperator() { }
    public static final DistinctOperator INSTANCE = new DistinctOperator();
    private static final List<String> PARAMETER_NAMES = List.of("by");

    @Override
    public String getName() {
        return "DISTINCT";
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
    public List<String> getFlags() {
        return IGNORE_CASE_FLAG;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        validateIgnoreCaseFlagUsage(1, call, holder);
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
