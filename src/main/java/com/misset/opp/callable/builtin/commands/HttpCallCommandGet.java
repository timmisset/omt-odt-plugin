package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;

public class HttpCallCommandGet extends BuiltInHttpCommand {
    private HttpCallCommandGet() {
    }

    public static final HttpCallCommandGet INSTANCE = new HttpCallCommandGet();

    @Override
    public String getName() {
        return "HTTP_GET";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        validateStringArgument(0, call, holder);
        validateJSONArgument(1, call, holder);
        validateBooleanArgument(2, call, holder);
    }
}
