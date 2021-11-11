package com.misset.opp.callable.builtin.commands;

public class HttpPutCommand extends BuiltInHttpCommand {
    private HttpPutCommand() { }
    public static final HttpPutCommand INSTANCE = new HttpPutCommand();

    @Override
    public String getName() {
        return "HTTP_PUT";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    public int maxNumberOfArguments() {
        return 4;
    }
}
