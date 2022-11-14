package com.misset.opp.odt.builtin.commands;

import java.util.List;

public class HttpCallCommandDelete extends HttpCallCommandGet {

    public static final HttpCallCommandDelete INSTANCE = new HttpCallCommandDelete();
    private static final List<String> PARAMETER_NAMES = List.of("url", "queryParams", "throwsOnError", "headers");

    @Override
    public String getName() {
        return "HTTP_DELETE";
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
