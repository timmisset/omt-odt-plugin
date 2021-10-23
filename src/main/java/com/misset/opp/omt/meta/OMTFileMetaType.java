package com.misset.opp.omt.meta;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.model.OMTModelMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTCommandsMetaType;
import com.misset.opp.omt.meta.model.scalars.scripts.ODTQueriesMetaType;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.addDefinedStatementsToMap;
import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.addImportStatementsToMap;

/**
 * The OMTFileMetaType is the root for all OMT features
 * Any .omt file that is analysed can contain these features
 */
public class OMTFileMetaType extends OMTMetaType implements OMTCallableProvider {
    public OMTFileMetaType(@NonNls @NotNull String typeName) {
        super(typeName);
    }

    protected static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("import", OMTImportMetaType::new);
        features.put("model", OMTModelMetaType::new);
        features.put("queries", ODTQueriesMetaType::new);
        features.put("commands", ODTCommandsMetaType::new);
        features.put("prefixes", OMTPrefixesMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getCallableMap(YAMLMapping yamlMapping) {
        /*
            The OMT File will provide the callables for the root elements queries, commands and import
         */
        final HashMap<String, List<PsiElement>> map = new HashMap<>();
        // first add the queries and commands, in case of shadowing the same name for imports, the defined statements are used
        addDefinedStatementsToMap(yamlMapping, "queries", map);
        addDefinedStatementsToMap(yamlMapping, "commands", map);

        final YAMLKeyValue imports = yamlMapping.getKeyValueByKey("import");
        if(imports != null && imports.getValue() instanceof YAMLMapping) {
            addImportStatementsToMap((YAMLMapping) imports.getValue(), map);
        }
        return map;
    }
}
