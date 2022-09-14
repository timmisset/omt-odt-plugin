package com.misset.opp.omt.commands;

import com.intellij.openapi.project.Project;

public class Rollback extends LocalCommand {
    public static final String CALLID = "@ROLLBACK";

    public Rollback(String source) {
        super(source);
    }

    @Override
    public String getName() {
        return "ROLLBACK";
    }

    @Override
    public String getDescription(Project project) {
        return String.format("Rollback all changes without ending the %s", getSource());
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
