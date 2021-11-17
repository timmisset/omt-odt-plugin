package com.misset.opp.omt.meta.model.scalars.references;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.psi.ODTOperatorCall;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTQueryOperationStep;
import com.misset.opp.odt.psi.ODTQueryPath;
import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Optional;

public class OMTPayloadQueryReferenceMetaType extends YamlStringType implements ODTInjectable {

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        final Boolean isValid = OMTProviderUtil.getInjectedContent(value, ODTQuery.class)
                .stream()
                .map(this::validateQuery)
                .findFirst()
                .orElse(false);
        if (!isValid) {
            problemsHolder.registerProblem(value, "Expecting a reference to a query", ProblemHighlightType.ERROR);
        }
    }

    private boolean validateQuery(ODTQuery query) {
        return Optional.of(query)
                .filter(ODTQueryPath.class::isInstance)
                .map(ODTQueryPath.class::cast)
                .map(ODTQueryPath::getQueryOperationStepList)
                .filter(list -> list.size() == 1)
                .map(list -> list.get(0))
                .map(ODTQueryOperationStep::getQueryStep)
                .map(ODTOperatorCall.class::isInstance)
                .orElse(false);
    }
}
