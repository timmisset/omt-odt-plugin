package com.misset.opp.callable.builtIn.commands;

public class ForkJoinCommand extends BuiltInCommand {
    private ForkJoinCommand() { }
    public static final ForkJoinCommand INSTANCE = new ForkJoinCommand();

    @Override
    public String getName() {
        return "FORKJOIN";
    }

    @Override
    public int minNumberOfArguments() {
        return -1;
    }
}
