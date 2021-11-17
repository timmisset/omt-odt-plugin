package com.misset.opp.callable.local;

public class GetErrorState extends LocalCommand {
    public static GetErrorState INSTANCE = new GetErrorState();
    protected GetErrorState() {}

    @Override
    public String getName() {
        return "GET_ERROR_STATE";
    }

    @Override
    public String getDescription(String context) {
        return String.format("Get the current error state for the %s", context);
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}
