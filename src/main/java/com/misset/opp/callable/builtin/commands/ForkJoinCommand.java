package com.misset.opp.callable.builtin.commands;

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
