package com.misset.opp.omt.commands;

import com.intellij.openapi.project.Project;

public class Commit extends LocalCommand {
    public static final String CALLID = "@COMMIT";

    public Commit(String source) {
        super(source);
    }

    @Override
    public String getName() {
        return "COMMIT";
    }

    @Override
    public String getDescription(Project project) {
        return String.format("Commit all changes without ending the %s", getSource());
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
