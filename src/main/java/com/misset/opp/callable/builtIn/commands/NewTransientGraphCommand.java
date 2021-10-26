package com.misset.opp.callable.builtIn.commands;

public class NewTransientGraphCommand extends BuiltInCommand {
    private NewTransientGraphCommand() { }
    public static final NewTransientGraphCommand INSTANCE = new NewTransientGraphCommand();

    @Override
    public String getName() {
        return "NEW_TRANSIENT_GRAPH";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }
}
