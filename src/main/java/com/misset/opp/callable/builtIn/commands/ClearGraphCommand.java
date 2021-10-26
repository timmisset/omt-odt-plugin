package com.misset.opp.callable.builtIn.commands;

public class ClearGraphCommand extends BuiltInCommand {
    private ClearGraphCommand() { }
    public static final ClearGraphCommand INSTANCE = new ClearGraphCommand();

    @Override
    public String getName() {
        return "CLEAR_GRAPH";
    }

    @Override
    public boolean isVoid() {
        return false;
    }
}
