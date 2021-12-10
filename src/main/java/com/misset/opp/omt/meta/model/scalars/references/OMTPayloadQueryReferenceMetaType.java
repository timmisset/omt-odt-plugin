package com.misset.opp.omt.meta.model.scalars.references;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Optional;

@SimpleInjectable
public class OMTPayloadQueryReferenceMetaType extends YamlStringType {

    @Override
    public void validateValue(@NotNull YAMLValue value,
                              @NotNull ProblemsHolder problemsHolder) {
        Optional<PsiElement> psiElement = Optional.ofNullable(value.getReference())
                .map(PsiReference::resolve);
        if (psiElement.isEmpty()) {
            // could not resolve this value, making it an invalid reference
            problemsHolder.registerProblem(value, "Requires a named reference to a query", ProblemHighlightType.ERROR);
        }
    }
}
