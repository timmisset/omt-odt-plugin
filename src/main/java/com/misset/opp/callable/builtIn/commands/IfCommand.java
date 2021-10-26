package com.misset.opp.callable.builtIn.commands;

public class IfCommand extends BuiltInCommand {
    private IfCommand() { }
    public static final IfCommand INSTANCE = new IfCommand();

    @Override
    public String getName() {
        return "IF";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }
}
