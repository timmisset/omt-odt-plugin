package com.misset.opp.odt.builtin.commands;

import java.util.List;

public class HttpCallCommandGet extends BuiltInHttpCommand {

    private static final List<String> PARAMETER_NAMES = List.of("url", "queryParams", "throwsOnError", "headers");

    protected HttpCallCommandGet() {
    }

    public static final HttpCallCommandGet INSTANCE = new HttpCallCommandGet();

    @Override
    public String getName() {
        return "HTTP_GET";
    }


    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
