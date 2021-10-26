package com.misset.opp.callable.builtin.commands;

public class LogCommand extends BuiltInCommand {
    private LogCommand() { }
    public static final LogCommand INSTANCE = new LogCommand();

    @Override
    public String getName() {
        return "LOG";
    }

    @Override
    public int minNumberOfArguments() {
        return -1;
    }
}
