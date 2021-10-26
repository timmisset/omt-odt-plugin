package com.misset.opp.callable.builtIn.commands;

public class NewCommand extends BuiltInCommand {
    private NewCommand() { }
    public static final NewCommand INSTANCE = new NewCommand();

    @Override
    public String getName() {
        return "NEW";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }
}
