package com.misset.opp.callable.local;

public class Done extends LocalCommand {
    protected Done() {}

    @Override
    public String getName() {
        return "DONE";
    }

    @Override
    public String getDescription(String context) {
        return String.format("Commit all changes and end the %s", context);
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
