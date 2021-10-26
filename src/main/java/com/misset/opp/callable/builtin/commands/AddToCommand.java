package com.misset.opp.callable.builtin.commands;

public class AddToCommand extends BuiltInCommand {
    private AddToCommand() { }
    public static final AddToCommand INSTANCE = new AddToCommand();

    @Override
    public String getName() {
        return "ADD_TO";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

}
