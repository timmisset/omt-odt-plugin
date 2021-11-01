package com.misset.opp.omt.meta;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
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
    private static final Key<CachedValue<MetaTypeProxy>> VALUE_META_TYPE = new Key<>("VALUE_META_TYPE");
    private static final Key<CachedValue<MetaTypeProxy>> KEY_VALUE_META_TYPE = new Key<>("KEY_VALUE_META_TYPE");

    private static final ModificationTracker YAML_MODIFICATION_TRACKER = ModificationTracker.EVER_CHANGED;

    /**
     * Required constructor for @Service implementation
     */
    public OMTMetaTypeProvider(Project _project) {
        super(getRoot::apply, YAML_MODIFICATION_TRACKER);
    }

    public static OMTMetaTypeProvider getInstance(@NotNull Project project) {
        return project.getService(OMTMetaTypeProvider.class);
    }

    // the (functional) interface ModelAccess uses getRoot to retrieve the root field
    private static final Function<YAMLDocument, Field> getRoot = (@NotNull YAMLDocument document) -> {
        String title = Optional.ofNullable(document.getName())
                .orElse(document.getContainingFile()
                        .getName());
        return new Field(title, new OMTFileMetaType(title));
    };

    @Override
    public @Nullable MetaTypeProxy getKeyValueMetaType(@NotNull YAMLKeyValue keyValue) {
        return CachedValuesManager.getCachedValue(keyValue, KEY_VALUE_META_TYPE, () -> {
            if (Optional.of(keyValue)
                    .map(YAMLKeyValue::getValue)
                    .map(YAMLValue::getTag)
                    .isEmpty()) {
                return new CachedValueProvider.Result<>(super.getKeyValueMetaType(keyValue),
                        keyValue.getContainingFile(),
                        YAML_MODIFICATION_TRACKER);
            }
            return new CachedValueProvider.Result<>(getTaggedKeyValueMetaType(keyValue),
                    keyValue.getContainingFile(),
                    YAML_MODIFICATION_TRACKER);
        });
    }

    @Override
    public @Nullable MetaTypeProxy getValueMetaType(@NotNull YAMLValue typedValue) {
        return CachedValuesManager.getCachedValue(typedValue, VALUE_META_TYPE, () -> {
            if (Optional.of(typedValue)
                    .map(YAMLValue::getTag)
                    .isEmpty()) {
                return new CachedValueProvider.Result<>(super.getValueMetaType(typedValue),
                        typedValue.getContainingFile(),
                        YAML_MODIFICATION_TRACKER);
            }
            return new CachedValueProvider.Result<>(getTaggedValueMetaType(typedValue),
                    typedValue.getContainingFile(),
                    YAML_MODIFICATION_TRACKER);
        });
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
