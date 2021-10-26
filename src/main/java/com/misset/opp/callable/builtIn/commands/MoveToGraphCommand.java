package com.misset.opp.callable.builtIn.commands;

public class MoveToGraphCommand extends BuiltInCommand {
    private MoveToGraphCommand() { }
    public static final MoveToGraphCommand INSTANCE = new MoveToGraphCommand();

    @Override
    public String getName() {
        return "MOVE_TO_GRAPH";
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
