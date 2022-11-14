package com.misset.opp.odt.builtin.commands;

import java.util.List;

public class HttpCallCommandPost extends BuiltInHttpCommand {

    private static final List<String> PARAMETER_NAMES = List.of("url", "body", "throwsOnError", "queryParams");

    private HttpCallCommandPost() {
    }

    public static final HttpCallCommandPost INSTANCE = new HttpCallCommandPost();

    @Override
    public String getName() {
        return "HTTP_POST";
    }

    @Override
    public int minNumberOfArguments() {
        return 2;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
