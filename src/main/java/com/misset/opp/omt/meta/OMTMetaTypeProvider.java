package com.misset.opp.omt.meta;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.psi.YAMLDocument;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Optional;
import java.util.function.Function;

@Service
public final class OMTMetaTypeProvider extends YamlMetaTypeProvider {
    /**
     * Required constructor for @Service implementation
     */
    public OMTMetaTypeProvider(Project _project) {
        super(getRoot::apply, ModificationTracker.NEVER_CHANGED);
    }

    public static OMTMetaTypeProvider getInstance(@NotNull Project project) {
        return project.getService(OMTMetaTypeProvider.class);
    }

    // the (functional) interface ModelAccess uses getRoot to retrieve the root field
    private static Function<YAMLDocument, Field> getRoot = (@NotNull YAMLDocument document) -> {
        String title = Optional.ofNullable(document.getName())
                .orElse(document.getContainingFile()
                        .getName());
        return new Field(title, new OMTFileMetaType(title));
    };

    @Override
    public @Nullable MetaTypeProxy getMetaTypeProxy(@NotNull PsiElement psi) {
        return super.getMetaTypeProxy(psi);
    }

    @Override
    public @Nullable YAMLValue getMetaOwner(@NotNull PsiElement psi) {
        return super.getMetaOwner(psi);
    }

    @Override
    public @Nullable MetaTypeProxy getKeyValueMetaType(@NotNull YAMLKeyValue keyValue) {
        if (Optional.of(keyValue)
                .map(YAMLKeyValue::getValue)
                .map(YAMLValue::getTag)
                .isEmpty()) {
            return super.getKeyValueMetaType(keyValue);
        }
        return getTaggedKeyValueMetaType(keyValue);
    }

    @Override
    public @Nullable MetaTypeProxy getValueMetaType(@NotNull YAMLValue typedValue) {
        if (Optional.of(typedValue)
                .map(YAMLValue::getTag)
                .isEmpty()) {
            return super.getValueMetaType(typedValue);
        }
        return getTaggedValueMetaType(typedValue);
    }

    /*
        When a value is tagged ! the returned metatype proxy depends on the tag that is supplied
        this means the parent (meta-type) should be aware of valid tags

        key: !tag
            specificEntryForTag
     */
    @Nullable
    private MetaTypeProxy getTaggedValueMetaType(@NotNull YAMLValue typedValue) {
        final YAMLKeyValue parentOfType = PsiTreeUtil.getParentOfType(typedValue, YAMLKeyValue.class);
        if (parentOfType == null) {
            return null;
        }

        final MetaTypeProxy metaTypeProxy = getKeyValueMetaType(parentOfType);
        if (typedValue.getTag() != null &&
                metaTypeProxy != null &&
                metaTypeProxy.getMetaType() instanceof OMTMetaTaggedType) {

            final String tag = typedValue
                    .getTag()
                    .getText();

            final OMTMetaTaggedType taggedElementContainer = (OMTMetaTaggedType) metaTypeProxy.getMetaType();

            if (taggedElementContainer.isValidTag(tag)) {
                Field.Relation relation = Field.Relation.OBJECT_CONTENTS;
                final Field field = taggedElementContainer.getByTag(tag);
                return FieldAndRelation.forNullable(field, relation);
            }
        }
        return null;
    }

    private MetaTypeProxy getTaggedKeyValueMetaType(@NotNull YAMLKeyValue keyValue) {
        final YAMLKeyValue parentOfType = PsiTreeUtil.getParentOfType(keyValue, YAMLKeyValue.class);
        final MetaTypeProxy metaTypeProxy = getKeyValueMetaType(parentOfType);
        final Field featureByName = metaTypeProxy.getMetaType()
                .findFeatureByName(keyValue.getKeyText());

        Field.Relation relation = Field.Relation.OBJECT_CONTENTS;
        return FieldAndRelation.forNullable(featureByName, relation);
    }
}
