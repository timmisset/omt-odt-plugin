package com.misset.opp.callable.builtin.commands;

public class GuidCommand extends BuiltInCommand {
    private GuidCommand() { }
    public static final GuidCommand INSTANCE = new GuidCommand();

    @Override
    public String getName() {
        return "GUID";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }
}
