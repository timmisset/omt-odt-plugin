package com.misset.opp.omt.meta;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLTokenTypes;
import org.jetbrains.yaml.meta.model.CompletionContext;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.misset.opp.omt.completion.OMTCompletions.getPlaceholderToken;

public abstract class OMTMetaTaggedType<T extends YamlMetaType> extends OMTMetaType {

    private final String name;

    protected OMTMetaTaggedType(@NotNull String name) {
        super(name);
        this.name = name;
    }

    protected abstract HashMap<String, Supplier<T>> getTaggedTypes();

    public List<String> getAvailableTags() {
        return getTaggedTypes().keySet().stream().sorted().collect(Collectors.toList());
    }

    public boolean isValidTag(String tag) {
        return getTaggedTypes().containsKey(tag);
    }

    public T getDelegateByTag(String tag) {
        return getTaggedTypes().getOrDefault(tag, () -> null).get();
    }

    public Field getByTag(String tag) {
        return Optional.of(getDelegateByTag(tag))
                .map(yamlMetaType -> new Field(name, yamlMetaType))
                .orElse(null);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return new HashMap<>();
    }

    @Override
    public @NotNull List<? extends LookupElement> getValueLookups(@NotNull YAMLScalar insertedScalar,
                                                                  @Nullable CompletionContext completionContext) {
        if (suggestFeatures(insertedScalar)) {
            // for a sequence, the value completion the value completion is triggered even when there is already a tag
            // therefore, show the available attributes instead
            return Optional.ofNullable(PsiTreeUtil.prevVisibleLeaf(insertedScalar))
                    .map(PsiElement::getText)
                    .filter(this::isValidTag)
                    .map(this::getDelegateByTag)
                    .map(t -> t.computeKeyCompletions(null))
                    .stream()
                    .flatMap(Collection::stream)
                    .map(this::getFeatureLookup)
                    .collect(Collectors.toList());
        } else {
            return getAvailableTags().stream()
                    .map(this::getTagLookup)
                    .collect(Collectors.toList());
        }
    }

    private LookupElement getFeatureLookup(Field field) {
        return LookupElementBuilder.create(field.getName() + ":")
                .withPresentableText(field.getName())
                .withIcon(field.getLookupIcon())
                .withTypeText(field.getDefaultType().getDisplayName(), true);
    }

    private boolean suggestFeatures(@NotNull YAMLScalar insertedScalar) {
        return Optional.of(insertedScalar)
                .filter(scalar -> scalar.getParent() instanceof YAMLSequenceItem)
                .map(PsiTreeUtil::prevVisibleLeaf)
                .map(PsiElement::getNode)
                .map(ASTNode::getElementType)
                .map(YAMLTokenTypes.TAG::equals)
                .orElse(false);
    }

    private LookupElement getTagLookup(String s) {
        return LookupElementBuilder.create(s).withPresentableText(s.substring(1))
                .withLookupStrings(List.of(s, s.substring(1)));
    }

    @Override
    public @NotNull List<Field> computeKeyCompletions(@Nullable YAMLMapping existingMapping) {
        final Optional<String> tag;
        if (existingMapping == null) {
            tag = Optional.ofNullable(getPlaceholderToken())
                    .map(PsiTreeUtil::prevVisibleLeaf)
                    .map(PsiElement::getText);
        } else {
            tag = Optional.of(existingMapping)
                    .map(YAMLValue::getTag)
                    .map(PsiElement::getText);
        }
        return tag.filter(this::isValidTag)
                .map(this::getDelegateByTag)
                .map(delegate -> delegate.computeKeyCompletions(existingMapping))
                .orElse(Collections.emptyList());
    }
}
