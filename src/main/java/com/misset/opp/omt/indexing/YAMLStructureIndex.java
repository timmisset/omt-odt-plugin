package com.misset.opp.omt.indexing;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

import java.util.HashMap;
import java.util.function.Supplier;

public class YAMLStructureIndex {

    private static final Key<CachedValue<String>> FULL_PATH = new Key<>("FULL_PATH");
    private static final HashMap<String, YamlMetaTypeProvider.MetaTypeProxy> index = new HashMap<>();

    public static YamlMetaTypeProvider.MetaTypeProxy getMetaTypeProxyByFullName(YAMLPsiElement psiElement,
                                                                                Supplier<YamlMetaTypeProvider.MetaTypeProxy> orElse) {
        final String key = getFullyQualifiedPathWithFile(psiElement);
        if (index.containsKey(key)) {
            return index.get(key);
        }
        final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = orElse.get();
        index.put(key, metaTypeProxy);
        return metaTypeProxy;
    }

    /**
     * This method is duplicated from the YAMLUtil with the addition of adding !Tag information
     * which is required to return a valid structure path since the tag/type determines if certain structures are available
     * <p>
     * Finally, it re-uses calculated information from parents. For example:
     * a:
     * b:
     * c: 'hi'    <-- calculates path for c, b and a
     * d: 'bye'   <-- now only has to calculate value for d and prepend b and a from earlier calculation
     */
    @NotNull
    public static String getFullyQualifiedPathWithFile(@NotNull YAMLPsiElement target) {
        return CachedValuesManager.getCachedValue(target,
                FULL_PATH,
                () -> new CachedValueProvider.Result<>(calculate(target), target.getContainingFile()));
    }

    private static String calculate(@NotNull YAMLPsiElement yamlPsiElement) {
        final StringBuilder builder = new StringBuilder();
        PsiElement element = yamlPsiElement;
        while (element != null) {
            YAMLPsiElement parent = PsiTreeUtil.getParentOfType(element, YAMLKeyValue.class, YAMLSequenceItem.class);
            String parentPath = getParentPath(parent);
            if (parentPath == null && parent != null) {
                parentPath = calculate(parent);
            }
            if (element instanceof YAMLKeyValue) {
                final YAMLKeyValue keyValue = (YAMLKeyValue) element;

                String keyValueIdentifier = (keyValue).getKeyText();
                if (keyValue.getValue() != null && keyValue.getValue().getTag() != null) {
                    keyValueIdentifier += keyValue.getValue().getTag().getText();
                }
                builder.insert(0, keyValueIdentifier);
                if (parent != null) {
                    builder.insert(0, '.');
                }
            } else if (element instanceof YAMLSequenceItem) {
                builder.insert(0, "[" + ((YAMLSequenceItem) element).getItemIndex() + "]");
            }
            if (parentPath != null) {
                builder.insert(0, parentPath);
                element = null;
            } else {
                element = parent;
            }
        }
        return builder.toString();
    }

    private static String getParentPath(PsiElement parent) {
        if (parent == null) {
            return null;
        }
        CachedValue<String> value = parent.getUserData(FULL_PATH);
        if (value != null) {
            Supplier<String> data = value.getUpToDateOrNull();
            if (data != null) {
                return data.get();
            }
        }
        return null;
    }

}
