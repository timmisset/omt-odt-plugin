package com.misset.opp.callable.builtin.commands;

import java.util.ArrayList;
import java.util.List;

public class ForEachCommand extends BuiltInCommand {
    private ForEachCommand() { }
    public static final ForEachCommand INSTANCE = new ForEachCommand();
    public static final List<LocalVariable> variables = new ArrayList<>();
    static {
        variables.add(new LocalVariable("$value", "the value from the collection"));
        variables.add(new LocalVariable("$index", "the index of the value in the collection"));
        variables.add(new LocalVariable("$array", "the entire values collection"));
    }

    @Override
    public String getName() {
        return "FOREACH";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public List<LocalVariable> getLocalVariables() {
        return variables;
    }
}
