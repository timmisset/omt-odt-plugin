package com.misset.opp.callable.builtin.commands;

public class HttpGetCommand extends BuiltInCommand {
    private HttpGetCommand() { }
    public static final HttpGetCommand INSTANCE = new HttpGetCommand();

    @Override
    public String getName() {
        return "HTTP_GET";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public int maxNumberOfArguments() {
        return 3;
    }
}
