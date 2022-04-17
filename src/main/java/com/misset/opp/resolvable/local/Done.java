package com.misset.opp.resolvable.local;

import com.intellij.openapi.project.Project;

public class Done extends LocalCommand {
    public static Done INSTANCE = new Done();

    protected Done() {
    }

    @Override
    public String getName() {
        return "DONE";
    }

    @Override
    public String getDescription(String context, Project project) {
        return String.format("Commit all changes and end the %s", context);
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
