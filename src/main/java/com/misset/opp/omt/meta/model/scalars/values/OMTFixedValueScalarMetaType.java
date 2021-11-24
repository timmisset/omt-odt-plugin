package com.misset.opp.omt.meta.model.scalars.values;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInspection.ProblemsHolder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.CompletionContext;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class OMTFixedValueScalarMetaType extends YamlScalarType {
    protected OMTFixedValueScalarMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    abstract Set<String> getAcceptableValues();

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        if (!getAcceptableValues().contains(scalarValue.getTextValue())) {
            holder.registerProblem(scalarValue,
                    String.format("Illegal value, acceptable values are: %s",
                            String.join(", ", getAcceptableValues())));
        }
    }

    @Override
    public @NotNull List<? extends LookupElement> getValueLookups(@NotNull YAMLScalar insertedScalar,
                                                                  @Nullable CompletionContext completionContext) {
        return getAcceptableValues().stream()
                .map(LookupElementBuilder::create)
                .collect(Collectors.toList());
    }

    @Override
    public @NotNull String getDisplayName() {
        return getAcceptableValues().stream()
                .sorted()
                .collect(Collectors.joining(" | "));
    }
}
