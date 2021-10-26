package com.misset.opp.callable.builtIn.commands;

public class CopyInGraphCommand extends BuiltInCommand {
    private CopyInGraphCommand() {
    }

    public static final CopyInGraphCommand INSTANCE = new CopyInGraphCommand();

    @Override
    public String getName() {
        return "COPY_IN_GRAPH";
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
