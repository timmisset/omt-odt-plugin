package com.misset.opp.callable.local;

public class Commit extends LocalCommand {
    protected Commit() {}

    @Override
    public String getName() {
        return "COMMIT";
    }

    @Override
    public String getDescription(String context) {
        return String.format("Commit all changes without ending the %s", context);
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
