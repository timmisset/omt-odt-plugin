package com.misset.opp.omt.meta;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.misset.opp.omt.meta.model.OMTModelMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.scalars.scripts.OMTCommandsMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTQueriesMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.*;

/**
 * The OMTFileMetaType is the root for all OMT features
 * Any .omt file that is analysed can contain these features
 */
public class OMTFileMetaType extends OMTMetaType implements OMTCallableProvider, OMTPrefixProvider {
    private static final Logger LOGGER = Logger.getInstance(OMTFileMetaType.class);
    public static final Key<CachedValue<HashMap<String, List<PsiCallable>>>> IMPORTED = new Key<>("IMPORTED");

    public OMTFileMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    protected static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("import", OMTImportMetaType::new);
        features.put("model", OMTModelMetaType::new);
        features.put("queries", () -> new OMTQueriesMetaType(true));
        features.put("commands", () -> new OMTCommandsMetaType(true));
        features.put("prefixes", OMTPrefixesMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiCallable>> getCallableMap(YAMLMapping yamlMapping,
                                                                      PsiLanguageInjectionHost host) {
        return LoggerUtil.computeWithLogger(LOGGER,
                "getCallableMap " + getFilename(yamlMapping),
                () -> {
                    final HashMap<String, List<PsiCallable>> map = new HashMap<>();
                    map.putAll(getDeclaredCallableMap(yamlMapping, host));
                    map.putAll(getImportingMembers(yamlMapping));
                    return map;
                });
    }

    private static String getFilename(PsiElement element) {
        return element.getContainingFile().getName();
    }

    public static @NotNull HashMap<String, List<PsiCallable>> getDeclaredCallableMap(YAMLMapping mapping,
                                                                                     PsiLanguageInjectionHost host) {
        final YAMLKeyValue model = mapping.getKeyValueByKey("model");
        return LoggerUtil.computeWithLogger(LOGGER, "getDeclaredCallableMap " + getFilename(mapping), () -> {
        /*
            The OMT File will provide the callables for the root elements queries, commands and import
         */
            final HashMap<String, List<PsiCallable>> map = new HashMap<>();
            // first add the queries and commands, in case of shadowing the same name for imports, the defined statements are used
            addInjectedCallablesToMap(mapping, "queries", map, host);
            addInjectedCallablesToMap(mapping, "commands", map, host);
            if (model != null && model.getValue() instanceof YAMLMapping) {
                addModelItemsToMap((YAMLMapping) model.getValue(), map);
            }
            return map;

        });
    }

    public static @NotNull HashMap<String, List<PsiCallable>> getImportingMembers(YAMLMapping yamlMapping) {
        final OMTFile containingFile = (OMTFile) yamlMapping.getContainingFile();
        return LoggerUtil.computeWithLogger(LOGGER, "getImportingMembers " + getFilename(yamlMapping),
                () -> CachedValuesManager.getCachedValue(containingFile,
                        IMPORTED,
                        () -> new CachedValueProvider.Result<>(getImportingMembers(containingFile), containingFile)));
    }

    private static HashMap<String, List<PsiCallable>> getImportingMembers(OMTFile containingFile) {
        final YAMLKeyValue imports = containingFile.getRootMapping().getKeyValueByKey("import");
        final HashMap<String, List<PsiCallable>> map = new HashMap<>();
        if (imports != null && imports.getValue() instanceof YAMLMapping) {
            addImportStatementsToMap((YAMLMapping) imports.getValue(), map);
        }
        return map;
    }

}
