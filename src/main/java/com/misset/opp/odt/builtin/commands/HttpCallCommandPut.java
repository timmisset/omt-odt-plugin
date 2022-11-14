package com.misset.opp.odt.builtin.commands;

import java.util.List;

public class HttpCallCommandPut extends BuiltInHttpCommand {

    private static final List<String> PARAMETER_NAMES = List.of("url", "body", "throwsOnError", "queryParams");

    private HttpCallCommandPut() {
    }

    public static final HttpCallCommandPut INSTANCE = new HttpCallCommandPut();

    @Override
    public String getName() {
        return "HTTP_PUT";
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
