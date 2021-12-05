package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;

public class ConcatOperator extends BuiltInStringOperator {
    private ConcatOperator() { }
    public static final ConcatOperator INSTANCE = new ConcatOperator();

    @Override
    public String getName() {
        return "CONCAT";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }
}
