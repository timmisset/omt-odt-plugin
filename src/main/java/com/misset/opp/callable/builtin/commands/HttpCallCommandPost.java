package com.misset.opp.callable.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;

public class HttpCallCommandPost extends BuiltInHttpCommand {
    private HttpCallCommandPost() {
    }

    public static final HttpCallCommandPost INSTANCE = new HttpCallCommandPost();

    @Override
    public String getName() {
        return "HTTP_POST";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public int maxNumberOfArguments() {
        return 4;
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        validateStringArgument(0, call, holder);
        validateJSONArgument(1, call, holder);
        validateBooleanArgument(2, call, holder);
        validateJSONArgument(3, call, holder);
    }
}
