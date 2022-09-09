package com.misset.opp.resolvable.local;

import com.intellij.openapi.project.Project;

public class Cancel extends LocalCommand {
    public static final String CALLID = "@CANCEL";

    public Cancel(String source) {
        super(source);
    }

    @Override
    public String getName() {
        return "CANCEL";
    }

    @Override
    public String getDescription(Project project) {
        return String.format("Dismiss all changes and end the %s", getSource());
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
