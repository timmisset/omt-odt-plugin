package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.model.handlers.OMTMergeHandlerMetaType;
import com.misset.opp.omt.meta.providers.OMTLocalCommandProvider;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTReasonMetaType;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.local.Commit;
import com.misset.opp.resolvable.local.LocalCommand;
import com.misset.opp.resolvable.local.Rollback;
import com.misset.opp.resolvable.psi.PsiResolvableScript;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTPrefixProviderUtil.addPrefixesToMap;
import static com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil.addSequenceToMap;

public class OMTProcedureMetaType extends OMTParameterizedModelItemMetaType implements
        OMTVariableProvider,
        OMTPrefixProvider,
        OMTLocalCommandProvider,
        OMTMetaCallable,
        OMTDocumented {
    protected OMTProcedureMetaType() {
        super("OMT Procedure");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Key<Boolean> UNRESOLVABLE = new Key<>("Unresolvable");
    private static final Key<Long> UNRESOLVABLE_TIMESTAMP = new Key<>("Timestamp");

    static {
        features.put("params", OMTParamsArrayMetaType::new);
        features.put("variables", OMTVariablesArrayMetaType::new);
        features.put("graphs", OMTGraphSelectionMetaType::new);
        features.put("prefixes", OMTPrefixesMetaType::new);
        features.put("onRun", OMTScriptMetaType::new);
        features.put("handlers", OMTMergeHandlerMetaType::new);
        features.put("reason", OMTReasonMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, List<PsiElement>> variableMap = new HashMap<>();
        addSequenceToMap(mapping, "variables", variableMap);
        addSequenceToMap(mapping, "params", variableMap, true);

        return variableMap;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getPrefixMap(YAMLMapping yamlMapping) {
        HashMap<String, List<PsiElement>> map = new HashMap<>();
        addPrefixesToMap(yamlMapping, "prefixes", map);
        return map;
    }

    @Override
    public HashMap<String, LocalCommand> getLocalCommandsMap() {
        final HashMap<String, LocalCommand> map = new HashMap<>();
        map.put(Commit.INSTANCE.getCallId(), Commit.INSTANCE);
        map.put(Rollback.INSTANCE.getCallId(), Rollback.INSTANCE);
        return map;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof OMTProcedureMetaType;
    }

    @Override
    public String getType() {
        return "PROCEDURE";
    }

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping, Context context) {
        if (UNRESOLVABLE.get(mapping, false) && UNRESOLVABLE_TIMESTAMP.get(mapping, -1L).equals(
                mapping.getContainingFile().getModificationStamp())) {
            return Collections.emptySet();
        }
        Set<OntResource> resources = Optional.ofNullable(mapping.getKeyValueByKey("onRun"))
                .map(YAMLKeyValue::getValue)
                .map(value -> OMTProviderUtil.getInjectedContent(value, PsiResolvableScript.class))
                .stream()
                .flatMap(Collection::stream)
                .map(Resolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
        if (resources.isEmpty()) {
            UNRESOLVABLE.set(mapping, true);
            UNRESOLVABLE_TIMESTAMP.set(mapping, mapping.getContainingFile().getModificationStamp());
        }
        return resources;
    }

    @Override
    public boolean isVoid(YAMLMapping mapping) {
        return mapping.getKeyValueByKey("onRun") != null;
    }

    @Override
    public boolean canBeAppliedTo(YAMLMapping mapping, Set<OntResource> resources) {
        return false;
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        // $committed value
        return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
    }

    @Override
    public List<String> getFlags() {
        return List.of("!nested", "!sibling", "!siblingCommit", "!autonomous");
    }


    @Override
    public String getDocumentationClass() {
        return "Procedure";
    }
}
