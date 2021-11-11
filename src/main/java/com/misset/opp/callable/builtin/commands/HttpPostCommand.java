package com.misset.opp.callable.builtin.commands;

public class HttpPostCommand extends BuiltInHttpCommand {
    private HttpPostCommand() { }
    public static final HttpPostCommand INSTANCE = new HttpPostCommand();

    @Override
    public String getName() {
        return "HTTP_POST";
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
