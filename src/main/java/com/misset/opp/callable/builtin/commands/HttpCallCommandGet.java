package com.misset.opp.callable.builtin.commands;

public class HttpCallCommandGet extends BuiltInHttpCommand {
    private HttpCallCommandGet() {
    }

    public static final HttpCallCommandGet INSTANCE = new HttpCallCommandGet();

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
