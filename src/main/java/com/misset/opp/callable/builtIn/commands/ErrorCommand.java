package com.misset.opp.callable.builtIn.commands;

public class ErrorCommand extends BuiltInCommand {
    private ErrorCommand() { }
    public static final ErrorCommand INSTANCE = new ErrorCommand();

    @Override
    public String getName() {
        return "ERROR";
    }

    @Override
    public int minNumberOfArguments() {
        return -1;
    }
}
