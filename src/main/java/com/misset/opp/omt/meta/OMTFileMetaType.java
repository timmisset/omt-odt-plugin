package com.misset.opp.omt.meta;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.model.OMTModelMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.scalars.scripts.OMTExportableCommandsMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTExportableQueriesMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.references.OMTDeclaredInterfaceReference;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.*;

/**
 * The OMTFileMetaType is the root for all OMT features
 * Any .omt file that is analysed can contain these features
 */
public class OMTFileMetaType extends OMTMetaType implements OMTCallableProvider, OMTPrefixProvider, OMTDocumented {
    private static final Logger LOGGER = Logger.getInstance(OMTFileMetaType.class);
    public static final Key<CachedValue<HashMap<String, List<PsiCallable>>>> IMPORTED = new Key<>("IMPORTED");
    private static final OMTFileMetaType INSTANCE = new OMTFileMetaType();

    public static OMTFileMetaType getInstance() {
        return INSTANCE;
    }

    protected OMTFileMetaType() {
        super("OMTFile");
    }

    protected static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("import", OMTImportMetaType::getInstance);
        features.put("model", OMTModelMetaType::getInstance);
        features.put("queries", OMTExportableQueriesMetaType::getInstance);
        features.put("commands", OMTExportableCommandsMetaType::getInstance);
        features.put("prefixes", OMTPrefixesMetaType::getInstance);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiCallable>> getCallableMap(YAMLMapping yamlMapping,
                                                                      PsiLanguageInjectionHost host) {
        String filename = getFilename(yamlMapping);
        return LoggerUtil.computeWithLogger(LOGGER,
                "getCallableMap " + filename,
                () -> {
                    final HashMap<String, List<PsiCallable>> map = new HashMap<>();
                    if (filename.endsWith(".interface.omt")) {
                        map.putAll(getInterfaceMap(yamlMapping));
                    } else {
                        map.putAll(getDeclaredCallableMap(yamlMapping, host));
                        map.putAll(getImportingMembers(yamlMapping));
                    }
                    return map;
                });
    }

    private Map<String, List<PsiCallable>> getInterfaceMap(YAMLMapping mapping) {
        return PsiTreeUtil.findChildrenOfType(mapping, YAMLKeyValue.class)
                .stream()
                .map(PsiElement::getReference)
                .filter(OMTDeclaredInterfaceReference.class::isInstance)
                .map(reference -> ((OMTDeclaredInterfaceReference) reference).multiResolve(false))
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .map(ResolveResult::getElement)
                .filter(PsiCallable.class::isInstance)
                .map(PsiCallable.class::cast)
                .collect(Collectors.groupingBy(Callable::getCallId));
    }

    private String getFilename(PsiElement element) {
        return element.getContainingFile().getName();
    }

    public @NotNull HashMap<String, List<PsiCallable>> getDeclaredCallableMap(YAMLMapping mapping,
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

    public @NotNull HashMap<String, List<PsiCallable>> getImportingMembers(YAMLMapping yamlMapping) {
        PsiFile containingFile = yamlMapping.getContainingFile();
        if (!(containingFile instanceof OMTFile)) {
            return new HashMap<>();
        }
        final OMTFile omtFile = (OMTFile) containingFile;
        return LoggerUtil.computeWithLogger(LOGGER, "getImportingMembers " + getFilename(yamlMapping),
                () -> CachedValuesManager.getCachedValue(omtFile,
                        IMPORTED,
                        () -> new CachedValueProvider.Result<>(getImportingMembers(omtFile), omtFile)));
    }

    private HashMap<String, List<PsiCallable>> getImportingMembers(OMTFile containingFile) {
        final YAMLKeyValue imports = containingFile.getRootMapping().getKeyValueByKey("import");
        final HashMap<String, List<PsiCallable>> map = new HashMap<>();
        if (imports != null && imports.getValue() instanceof YAMLMapping) {
            addImportStatementsToMap((YAMLMapping) imports.getValue(), map);
        }
        return map;
    }

    @Override
    public String getDocumentationClass() {
        return "Document constructs";
    }

    @Override
    public String getLevel1Header() {
        return "Documents";
    }
}
