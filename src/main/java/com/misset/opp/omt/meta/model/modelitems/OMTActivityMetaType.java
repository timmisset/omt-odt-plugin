package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.omt.commands.*;
import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.arrays.OMTHandlersArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTVariablesArrayMetaType;
import com.misset.opp.omt.meta.arrays.OMTWatchersArrayMetaType;
import com.misset.opp.omt.meta.model.*;
import com.misset.opp.omt.meta.providers.OMTCallableProvider;
import com.misset.opp.omt.meta.providers.OMTLocalCommandProvider;
import com.misset.opp.omt.meta.providers.OMTPrefixProvider;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.omt.meta.providers.util.OMTPrefixProviderUtil;
import com.misset.opp.omt.meta.scalars.OMTInterpolatedStringMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTCommandsMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTQueriesMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTReasonMetaType;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiPrefix;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.*;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.addInjectedCallablesToMap;
import static com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil.addSequenceToMap;

public class OMTActivityMetaType extends OMTParameterizedModelItemMetaType implements
        OMTVariableProvider,
        OMTCallableProvider,
        OMTPrefixProvider,
        OMTLocalCommandProvider,
        OMTMetaCallable,
        OMTDocumented {

    public static final String ACTIVITY = "Activity";

    protected OMTActivityMetaType() {
        super("OMT Activity");
    }

    private static final OMTActivityMetaType INSTANCE = new OMTActivityMetaType();

    public static OMTActivityMetaType getInstance() {
        return INSTANCE;
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    public static final String RETURNS = "returns";

    static {
        features.put("title", OMTInterpolatedStringMetaType::getInstance);
        features.put("onDefaultClose", OMTInterpolatedStringMetaType::getInstance);
        features.put("params", OMTParamsArrayMetaType::getInstance);
        features.put("variables", OMTVariablesArrayMetaType::getInstance);
        features.put("handlers", OMTHandlersArrayMetaType::getInstance);
        features.put("graphs", OMTGraphSelectionMetaType::getInstance);
        features.put("watchers", OMTWatchersArrayMetaType::getInstance);
        features.put("rules", OMTRulesMetaType::getInstance);
        features.put("prefixes", OMTPrefixesMetaType::getInstance);
        features.put("queries", OMTQueriesMetaType::getInstance);
        features.put("commands", OMTCommandsMetaType::getInstance);
        features.put("onStart", OMTScriptMetaType::getInstance);
        features.put("onCommit", OMTScriptMetaType::getInstance);
        features.put("onCancel", OMTScriptMetaType::getInstance);
        features.put("onDone", OMTScriptMetaType::getInstance);
        features.put(RETURNS, OMTQueryMetaType::getInstance);
        features.put("actions", OMTActionsMapMetaType::getInstance);
        features.put("reason", OMTReasonMetaType::getInstance);
        features.put("payload", OMTPayloadMetaType::getInstance);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, Collection<PsiVariable>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, Collection<PsiVariable>> map = new HashMap<>();
        addSequenceToMap(mapping, "variables", map);
        addSequenceToMap(mapping, "params", map);
        return map;
    }

    @Override
    public @NotNull HashMap<String, Collection<PsiCallable>> getCallableMap(YAMLMapping yamlMapping,
                                                                            PsiLanguageInjectionHost host) {
        HashMap<String, Collection<PsiCallable>> map = new HashMap<>();
        addInjectedCallablesToMap(yamlMapping, "commands", map, host);
        addInjectedCallablesToMap(yamlMapping, "queries", map, host);
        return map;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public HashMap<String, LocalCommand> getLocalCommandsMap() {
        final HashMap<String, LocalCommand> map = new HashMap<>();
        map.put(Cancel.CALLID, new Cancel(ACTIVITY));
        map.put(Commit.CALLID, new Commit(ACTIVITY));
        map.put(Done.CALLID, new Done(ACTIVITY));
        map.put(GetErrorState.CALLID, new GetErrorState(ACTIVITY));
        map.put(HasError.CALLID, new HasError(ACTIVITY));
        map.put(Rollback.CALLID, new Rollback(ACTIVITY));
        return map;
    }

    @Override
    public CallableType getType() {
        return CallableType.ACTIVITY;
    }

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping, @Nullable Context context) {
        if (isVoid(mapping)) {
            return Collections.emptySet();
        } else {
            return Optional.ofNullable(mapping.getKeyValueByKey(RETURNS))
                    .map(YAMLKeyValue::getValue)
                    .map(value -> OMTODTInjectionUtil.getInjectedContent(value, ODTQuery.class))
                    .stream()
                    .flatMap(Collection::stream)
                    .map(Resolvable::resolve)
                    .filter(resources -> !resources.isEmpty())
                    .findFirst()
                    .orElse(Set.of(OntologyModelConstants.getOwlThingInstance()));
        }
    }

    @Override
    public boolean isVoid(YAMLMapping mapping) {
        return mapping.getKeyValueByKey(RETURNS) == null;
    }

    @Override
    public boolean canBeAppliedTo(YAMLMapping mapping, Set<OntResource> resources) {
        return false;
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        // $committed value
        return Set.of(OntologyModelConstants.getXsdBooleanInstance());
    }

    @Override
    public List<String> getFlags() {
        return List.of("!nested", "!sibling", "!siblingCommit", "!autonomous", "!dialog");
    }

    @Override
    public String getDocumentationClass() {
        return ACTIVITY;
    }

    @Override
    public @NotNull Map<String, Collection<PsiPrefix>> getPrefixMap(YAMLMapping yamlMapping) {
        return OMTPrefixProviderUtil.getPrefixMap(yamlMapping);
    }

    @Override
    public @NotNull Map<String, String> getNamespaces(YAMLMapping yamlMapping) {
        return OMTPrefixProviderUtil.getNamespaces(yamlMapping);
    }

    @Override
    public void validateValue(@NotNull YAMLValue value, @NotNull ProblemsHolder problemsHolder) {
        Optional.of(value)
                .filter(YAMLMapping.class::isInstance)
                .map(YAMLMapping.class::cast)
                .map(mapping -> mapping.getKeyValueByKey("handlers"))
                .map(YAMLKeyValue::getValue)
                .ifPresent(handlers -> OMTHandlersArrayMetaType.getInstance().validateValue(handlers, problemsHolder));
    }
}
