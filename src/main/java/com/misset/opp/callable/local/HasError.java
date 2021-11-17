package com.misset.opp.callable.local;

public class HasError extends LocalCommand {
    public static HasError INSTANCE = new HasError();
    protected HasError() {}

    @Override
    public String getName() {
        return "GET_ERROR_STATE";
    }

    @Override
    public String getDescription(String context) {
        return String.format("Check if there is an error for the %s", context);
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}
