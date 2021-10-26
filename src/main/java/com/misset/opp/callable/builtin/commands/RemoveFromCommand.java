package com.misset.opp.callable.builtin.commands;

public class RemoveFromCommand extends BuiltInCommand {
    private RemoveFromCommand() { }
    public static final RemoveFromCommand INSTANCE = new RemoveFromCommand();

    @Override
    public String getName() {
        return "REMOVE_FROM";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

}
