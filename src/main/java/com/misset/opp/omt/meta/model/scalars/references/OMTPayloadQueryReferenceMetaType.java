package com.misset.opp.omt.meta.model.scalars.references;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiResolvable;
import com.misset.opp.omt.meta.OMTInjectable;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLValue;

@SimpleInjectable
public class OMTPayloadQueryReferenceMetaType extends YamlStringType implements OMTInjectable {

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        final boolean isValid = OMTProviderUtil.getInjectedContent(value, PsiResolvable.class)
                .stream()
                .allMatch(PsiResolvable::isReference);
        if (!isValid) {
            problemsHolder.registerProblem(value, "Expecting a reference to a query", ProblemHighlightType.ERROR);
        }
    }
}
