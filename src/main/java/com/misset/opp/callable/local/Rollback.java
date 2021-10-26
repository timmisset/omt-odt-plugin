package com.misset.opp.callable.local;

public class Rollback extends LocalCommand {
    protected Rollback() {}

    @Override
    public String getName() {
        return "ROLLBACK";
    }

    @Override
    public String getDescription(String context) {
        return String.format("Rollback all changes without ending the %s", context);
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
