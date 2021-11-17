package com.misset.opp.callable.local;

public class Cancel extends LocalCommand {
    public static Cancel INSTANCE = new Cancel();
    protected Cancel() {}

    @Override
    public String getName() {
        return "CANCEL";
    }

    @Override
    public String getDescription(String context) {
        return String.format("Dismiss all changes and end the %s", context);
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
