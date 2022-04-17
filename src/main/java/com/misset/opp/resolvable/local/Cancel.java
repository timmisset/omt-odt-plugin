package com.misset.opp.resolvable.local;

import com.intellij.openapi.project.Project;

public class Cancel extends LocalCommand {
    public static Cancel INSTANCE = new Cancel();

    protected Cancel() {
    }

    @Override
    public String getName() {
        return "CANCEL";
    }

    @Override
    public String getDescription(String context, Project project) {
        return String.format("Dismiss all changes and end the %s", context);
    }

    @Override
    public boolean isVoid() {
        return true;
    }
}
