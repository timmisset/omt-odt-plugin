package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.openapi.util.Key;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvableScript;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayMetaType;
import com.misset.opp.omt.meta.model.OMTGraphSelectionMetaType;
import com.misset.opp.omt.meta.model.OMTPrefixesMetaType;
import com.misset.opp.omt.meta.model.handlers.OMTMergeHandlerMetaType;
import com.misset.opp.omt.meta.providers.OMTLocalCommandProvider;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTReasonMetaType;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.local.Commit;
import com.misset.opp.resolvable.local.LocalCommand;
import com.misset.opp.resolvable.local.Rollback;
import com.misset.opp.resolvable.psi.PsiPrefix;
import com.misset.opp.resolvable.psi.PsiVariable;
import com.misset.opp.ttl.model.OppModelConstants;
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
    private static final OMTProcedureMetaType INSTANCE = new OMTProcedureMetaType();
    public static final String PROCEDURE = "Procedure";

    public static OMTProcedureMetaType getInstance() {
        return INSTANCE;
    }

    private OMTProcedureMetaType() {
        super("OMT Procedure");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Key<Boolean> UNRESOLVABLE = new Key<>("Unresolvable");
    private static final Key<Long> UNRESOLVABLE_TIMESTAMP = new Key<>("Timestamp");

    private static final String ON_RUN = "onRun";

    static {
        features.put("params", OMTParamsArrayMetaType::getInstance);
        features.put("variables", OMTVariablesArrayMetaType::getInstance);
        features.put("graphs", OMTGraphSelectionMetaType::getInstance);
        features.put("prefixes", OMTPrefixesMetaType::getInstance);
        features.put(ON_RUN, OMTScriptMetaType::getInstance);
        features.put("handlers", OMTMergeHandlerMetaType::getInstance);
        features.put("reason", OMTReasonMetaType::getInstance);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, Collection<PsiVariable>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, Collection<PsiVariable>> variableMap = new HashMap<>();
        addSequenceToMap(mapping, "variables", variableMap);
        addSequenceToMap(mapping, "params", variableMap);

        return variableMap;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public @NotNull Map<String, Collection<PsiPrefix>> getPrefixMap(YAMLMapping yamlMapping) {
        Map<String, Collection<PsiPrefix>> map = new HashMap<>();
        addPrefixesToMap(yamlMapping, "prefixes", map);
        return map;
    }

    @Override
    public HashMap<String, LocalCommand> getLocalCommandsMap() {
        final HashMap<String, LocalCommand> map = new HashMap<>();
        map.put(Commit.CALLID, new Commit(PROCEDURE));
        map.put(Rollback.CALLID, new Rollback(PROCEDURE));
        return map;
    }

    @Override
    public CallableType getType() {
        return CallableType.PROCEDURE;
    }

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping, Context context) {
        if (Boolean.TRUE.equals(UNRESOLVABLE.get(mapping, false)) && UNRESOLVABLE_TIMESTAMP.get(mapping, -1L).equals(
                mapping.getContainingFile().getModificationStamp())) {
            return Collections.emptySet();
        }
        Set<OntResource> resources = Optional.ofNullable(mapping.getKeyValueByKey(ON_RUN))
                .map(YAMLKeyValue::getValue)
                .map(value -> OMTODTInjectionUtil.getInjectedContent(value, ODTResolvableScript.class))
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
        return mapping.getKeyValueByKey(ON_RUN) != null;
    }

    @Override
    public boolean canBeAppliedTo(YAMLMapping mapping, Set<OntResource> resources) {
        return false;
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        // $committed value
        return Set.of(OppModelConstants.getXsdBooleanInstance());
    }

    @Override
    public List<String> getFlags() {
        return List.of("!nested", "!sibling", "!siblingCommit", "!autonomous");
    }


    @Override
    public String getDocumentationClass() {
        return PROCEDURE;
    }
}
