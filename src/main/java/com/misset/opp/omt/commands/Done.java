package com.misset.opp.omt.commands;

import com.intellij.openapi.project.Project;

public class Done extends LocalCommand {
    public static final String CALLID = "@DONE";

    public Done(String source) {
        super(source);
    }

    @Override
    public String getName() {
        return "DONE";
    }

    @Override
    public String getDescription(Project project) {
        return String.format("Commit all changes and end the %s", getSource());
    }

    @Override
    public boolean isVoid() {
        return true;
    }

    @Override
    public boolean isFinalCommand() {
        return true;
    }
}
