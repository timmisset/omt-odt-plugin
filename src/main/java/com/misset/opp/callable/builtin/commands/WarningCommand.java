package com.misset.opp.callable.builtin.commands;

public class WarningCommand extends BuiltInCommand {
    private WarningCommand() { }
    public static final WarningCommand INSTANCE = new WarningCommand();

    @Override
    public String getName() {
        return "WARNING";
    }

    @Override
    public int minNumberOfArguments() {
        return -1;
    }
}
