package com.misset.opp.callable.builtin.commands;

public class HttpCallCommandPut extends BuiltInHttpCommand {
    private HttpCallCommandPut() {
    }

    public static final HttpCallCommandPut INSTANCE = new HttpCallCommandPut();

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
