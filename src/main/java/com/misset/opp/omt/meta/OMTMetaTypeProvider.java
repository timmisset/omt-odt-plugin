package com.misset.opp.omt.meta;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.Field;
import org.jetbrains.yaml.meta.model.ModelAccess;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Optional;

@Service
public final class OMTMetaTypeProvider extends YamlMetaTypeProvider {
    private static final Key<CachedValue<MetaTypeProxy>> VALUE_META_TYPE = new Key<>("VALUE_META_TYPE");
    private static final Key<CachedValue<MetaTypeProxy>> KEY_VALUE_META_TYPE = new Key<>("KEY_VALUE_META_TYPE");
    private static final Key<CachedValue<MetaTypeProxy>> META_TYPE_PROXY = new Key<>("META_TYPE_PROXY");

    /**
     * The YamlMetaTypeProvider already provides the containing Yaml file as modification dependency.
     * This means that whenever anything in the file is changed, the meta cache is cleared anyway. No need
     * to provide anything more.
     *
     * @see YamlMetaTypeProvider#getValueMetaType(org.jetbrains.yaml.psi.YAMLValue)
     */
    private static final ModificationTracker YAML_MODIFICATION_TRACKER = ModificationTracker.NEVER_CHANGED;
    private static final ModelAccess modelAccess = document -> {
        String title = Optional.ofNullable(document.getContainingFile())
                .map(PsiFileSystemItem::getName)
                .orElse("an-omt-file");
        YamlMetaType root = title.endsWith("module.omt") ? new OMTModuleFileType(title) : new OMTFileMetaType(title);
        return new Field(title, root);
    };

    public OMTMetaTypeProvider() {
        super(modelAccess, YAML_MODIFICATION_TRACKER);
    }

    public static OMTMetaTypeProvider INSTANCE = null;
    public static OMTMetaTypeProvider getInstance(@NotNull Project project) {
        if (INSTANCE == null) {
            INSTANCE = project.getService(OMTMetaTypeProvider.class);
        }
        return INSTANCE;
    }

    @Override
    public @Nullable MetaTypeProxy getKeyValueMetaType(@NotNull YAMLKeyValue keyValue) {
        return CachedValuesManager.getCachedValue(keyValue, KEY_VALUE_META_TYPE, () -> {
            if (Optional.of(keyValue)
                    .map(YAMLKeyValue::getValue)
                    .map(YAMLValue::getTag)
                    .isEmpty()) {
                return new CachedValueProvider.Result<>(super.getKeyValueMetaType(keyValue),
                        keyValue.getContainingFile());
            }
            return new CachedValueProvider.Result<>(getTaggedKeyValueMetaType(keyValue),
                    keyValue.getContainingFile());
        });
    }

    public @Nullable YamlMetaType getResolvedKeyValueMetaTypeMeta(@NotNull YAMLKeyValue keyValue) {
        return Optional.ofNullable(getKeyValueMetaType(keyValue))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .orElse(null);
    }

    public @Nullable YamlMetaType getResolvedMetaType(@NotNull PsiElement element) {
        final MetaTypeProxy metaTypeProxy = getMetaTypeProxy(element);
        return metaTypeProxy != null ? metaTypeProxy.getMetaType() : null;
    }

    /**
     * The OMTMetaTypeProvider enriches the default YamlMetaTypeProvider by including support for
     * !Tag identifiers in the YamlValues.
     * Use the OMTMetaTaggedType to provide a list of acceptable tags and related meta-types
     */
    @Override
    public @Nullable MetaTypeProxy getValueMetaType(@NotNull YAMLValue typedValue) {
        return CachedValuesManager.getCachedValue(typedValue, VALUE_META_TYPE, () -> {
            if (Optional.of(typedValue)
                    .map(YAMLValue::getTag)
                    .isEmpty()) {
                return new CachedValueProvider.Result<>(super.getValueMetaType(typedValue),
                        typedValue.getContainingFile());
            }
            return new CachedValueProvider.Result<>(getTaggedValueMetaType(typedValue),
                    typedValue.getContainingFile());
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

    @Nullable
    @Override
    public MetaTypeProxy getMetaTypeProxy(@NotNull PsiElement element) {
        return CachedValuesManager.getCachedValue(element, META_TYPE_PROXY, () ->
                new CachedValueProvider.Result<>(super.getMetaTypeProxy(element), element.getContainingFile()));
    }

    @Nullable
    @Override
    public YAMLValue getMetaOwner(@NotNull PsiElement psi) {
        /*
            The original method in YamlMetaTypeProvider doesn't make sense.
            Traversing up the file is much more costly and will tell us nothing more than
            simply trying to obtain the YAMLValue directly, it will return null if the psi is not
            part of the YAML structure (which would probably never happen anyway)
         */
        return PsiTreeUtil.getParentOfType(psi, YAMLValue.class, false);
    }

    private MetaTypeProxy getTaggedKeyValueMetaType(@NotNull YAMLKeyValue keyValue) {
        final YAMLKeyValue parentOfType = PsiTreeUtil.getParentOfType(keyValue, YAMLKeyValue.class);
        if (parentOfType == null) {
            return null;
        }
        final MetaTypeProxy metaTypeProxy = getKeyValueMetaType(parentOfType);
        if (metaTypeProxy == null) {
            return null;
        }
        final Field featureByName = metaTypeProxy.getMetaType()
                .findFeatureByName(keyValue.getKeyText());

        Field.Relation relation = Field.Relation.OBJECT_CONTENTS;
        return FieldAndRelation.forNullable(featureByName, relation);
    }
}
