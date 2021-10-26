package com.misset.opp.callable.builtin.commands;

public class TimeStampCommand extends BuiltInCommand {
    private TimeStampCommand() { }
    public static final TimeStampCommand INSTANCE = new TimeStampCommand();

    @Override
    public String getName() {
        return "TIMESTAMP";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
