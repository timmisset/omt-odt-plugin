package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.omt.documentation.OMTDocumented;
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
import com.misset.opp.omt.meta.providers.util.OMTProviderUtil;
import com.misset.opp.omt.meta.scalars.OMTInterpolatedStringMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTCommandsMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTQueriesMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTReasonMetaType;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.local.*;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiResolvableQuery;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

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
    protected OMTActivityMetaType() {
        super("OMT Activity");
    }

    private static final OMTActivityMetaType INSTANCE = new OMTActivityMetaType();

    public static OMTActivityMetaType getInstance() {
        return INSTANCE;
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

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
        features.put("returns", OMTQueryMetaType::getInstance);
        features.put("actions", OMTActionsMapMetaType::getInstance);
        features.put("reason", OMTReasonMetaType::getInstance);
        features.put("payload", OMTPayloadMetaType::getInstance);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public @NotNull HashMap<String, List<PsiElement>> getVariableMap(YAMLMapping mapping) {
        HashMap<String, List<PsiElement>> map = new HashMap<>();
        addSequenceToMap(mapping, "variables", map);
        addSequenceToMap(mapping, "params", map, true);
        return map;
    }

    @Override
    public @NotNull HashMap<String, List<PsiCallable>> getCallableMap(YAMLMapping yamlMapping, PsiLanguageInjectionHost host) {
        HashMap<String, List<PsiCallable>> map = new HashMap<>();
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
        map.put(Cancel.INSTANCE.getCallId(), Cancel.INSTANCE);
        map.put(Commit.INSTANCE.getCallId(), Commit.INSTANCE);
        map.put(Done.INSTANCE.getCallId(), Done.INSTANCE);
        map.put(GetErrorState.INSTANCE.getCallId(), GetErrorState.INSTANCE);
        map.put(HasError.INSTANCE.getCallId(), HasError.INSTANCE);
        map.put(Rollback.INSTANCE.getCallId(), Rollback.INSTANCE);
        return map;
    }

    @Override
    public String getType() {
        return "ACTIVITY";
    }

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping, Context context) {
        if (isVoid(mapping)) {
            return Collections.emptySet();
        } else {
            return Optional.ofNullable(mapping.getKeyValueByKey("returns"))
                    .map(YAMLKeyValue::getValue)
                    .map(value -> OMTProviderUtil.getInjectedContent(value, PsiResolvableQuery.class))
                    .stream()
                    .flatMap(Collection::stream)
                    .map(Resolvable::resolve)
                    .filter(resources -> !resources.isEmpty())
                    .findFirst()
                    .orElse(Set.of(OppModelConstants.OWL_THING_INSTANCE));
        }
    }

    @Override
    public boolean isVoid(YAMLMapping mapping) {
        return mapping.getKeyValueByKey("returns") == null;
    }

    @Override
    public boolean canBeAppliedTo(YAMLMapping mapping, Set<OntResource> resources) {
        return false;
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        // $committed value
        return Set.of(OppModelConstants.XSD_BOOLEAN_INSTANCE);
    }

    @Override
    public List<String> getFlags() {
        return List.of("!nested", "!sibling", "!siblingCommit", "!autonomous", "!dialog");
    }

    @Override
    public String getDocumentationClass() {
        return "Activity";
    }
}
