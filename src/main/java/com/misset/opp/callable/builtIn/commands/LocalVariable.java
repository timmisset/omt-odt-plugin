package com.misset.opp.callable.builtIn.commands;

public class LocalVariable {

    private final String name;
    private final String description;
    public LocalVariable(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
