package com.misset.opp.callable.builtin.commands;

public class AssignCommand extends BuiltInCommand {
    private AssignCommand() { }
    public static final AssignCommand INSTANCE = new AssignCommand();
    @Override
    public String getName() {
        return "ASSIGN";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

}
