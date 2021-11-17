package com.misset.opp.callable.builtin.commands;

public class HttpCallCommandPost extends BuiltInHttpCommand {
    private HttpCallCommandPost() {
    }

    public static final HttpCallCommandPost INSTANCE = new HttpCallCommandPost();

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
