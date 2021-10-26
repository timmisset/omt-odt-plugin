package com.misset.opp.callable.builtin.commands;

public class SerialCommand extends BuiltInCommand {
    private SerialCommand() { }
    public static final SerialCommand INSTANCE = new SerialCommand();

    @Override
    public String getName() {
        return "SERIAL";
    }

    @Override
    public int minNumberOfArguments() {
        return -1;
    }
}
