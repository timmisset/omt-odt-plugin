package com.misset.opp.callable.builtIn.commands;

public class NewGraphCommand extends BuiltInCommand {
    private NewGraphCommand() { }
    public static final NewGraphCommand INSTANCE = new NewGraphCommand();

    @Override
    public String getName() {
        return "NEW_GRAPH";
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
