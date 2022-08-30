package com.misset.opp.omt.meta.scalars.values;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInspection.ProblemsHolder;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.CompletionContext;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class OMTFixedValueScalarMetaType extends YamlScalarType {
    protected OMTFixedValueScalarMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    abstract Set<String> getAcceptableValues();

    protected Map<String, String> getDescribedValues() {
        return Collections.emptyMap();
    }

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
        if (!getDescribedValues().isEmpty()) {
            return getDescribedValues().entrySet().stream()
                    .map(entry -> LookupElementBuilder.create(entry.getKey())
                            .withTailText(entry.getValue()))
                    .collect(Collectors.toList());
        } else {
            return getAcceptableValues().stream()
                    .map(LookupElementBuilder::create)
                    .collect(Collectors.toList());
        }

    }

    @Override
    public @NotNull String getDisplayName() {
        return getAcceptableValues().stream()
                .sorted()
                .collect(Collectors.joining(" | "));
    }
}
