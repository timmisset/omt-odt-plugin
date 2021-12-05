package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContainsOperator extends BuiltInBooleanOperator {
    private ContainsOperator() { }
    public static final ContainsOperator INSTANCE = new ContainsOperator();

    @Override
    public String getName() {
        return "CONTAINS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public List<String> getFlags() {
        return IGNORE_CASE_FLAG;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.getCallInputType(), holder, call);
        validateStringArgument(0, call, holder);
        validateBooleanArgument(1, call, holder);
        validateIgnoreCaseFlagUsage(1, call, holder);
    }
}
