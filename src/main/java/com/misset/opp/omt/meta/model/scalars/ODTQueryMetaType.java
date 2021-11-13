package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.ODTInjectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLValue;

public class ODTQueryMetaType extends YamlScalarType  implements ODTInjectable {
    public ODTQueryMetaType() {
        super("ODT Query");
    }

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        if(value.getText().endsWith(";")) {
            problemsHolder.registerProblem(value, "Query value should not contain semicolon ending", ProblemHighlightType.ERROR);
        }
    }
}
