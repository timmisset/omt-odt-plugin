package com.misset.opp.resolvable.local;

import com.intellij.openapi.project.Project;

public class Commit extends LocalCommand {
    public static Commit INSTANCE = new Commit();

    protected Commit() {
    }

    @Override
    public String getName() {
        return "COMMIT";
    }

    @Override
    public String getDescription(String context, Project project) {
        return String.format("Commit all changes without ending the %s", context);
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
