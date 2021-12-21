package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
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
import com.misset.opp.omt.meta.scalars.OMTInterpolatedStringMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTQueryMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTCommandsMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTQueriesMetaType;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTReasonMetaType;
import com.misset.opp.resolvable.local.*;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import static com.misset.opp.omt.meta.providers.util.OMTCallableProviderUtil.addInjectedCallablesToMap;
import static com.misset.opp.omt.meta.providers.util.OMTVariableProviderUtil.addSequenceToMap;

public class OMTActivityMetaType extends OMTModelItemDelegateMetaType implements
        OMTVariableProvider,
        OMTCallableProvider,
        OMTPrefixProvider,
        OMTLocalCommandProvider,
        OMTMetaCallable {
    protected OMTActivityMetaType() {
        super("OMT Activity");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("title", OMTInterpolatedStringMetaType::new);
        features.put("onDefaultClose", OMTInterpolatedStringMetaType::new);
        features.put("params", OMTParamsArrayMetaType::new);
        features.put("variables", OMTVariablesArrayMetaType::new);
        features.put("handlers", OMTHandlersArrayMetaType::new);
        features.put("graphs", OMTGraphSelectionMetaType::new);
        features.put("watchers", OMTWatchersArrayMetaType::new);
        features.put("rules", OMTRulesMetaType::new);
        features.put("prefixes", OMTPrefixesMetaType::new);
        features.put("queries", OMTQueriesMetaType::new);
        features.put("commands", OMTCommandsMetaType::new);
        features.put("onStart", OMTScriptMetaType::new);
        features.put("onCommit", OMTScriptMetaType::new);
        features.put("onCancel", OMTScriptMetaType::new);
        features.put("onDone", OMTScriptMetaType::new);
        features.put("returns", OMTQueryMetaType::new);
        features.put("actions", OMTActionsMetaType::new);
        features.put("reason", OMTReasonMetaType::new);
        features.put("payload", OMTPayloadMetaType::new);
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
    public Set<OntResource> resolve(YAMLMapping mapping, Set<OntResource> resources, PsiCall call) {
        // todo:
        // calculate the possible outcomes from the Activity, can be done if the activity has a 'returns' field
        return Set.of(OppModel.INSTANCE.OWL_THING_INSTANCE);
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
        return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
    }

    @Override
    public List<String> getFlags() {
        return List.of("!nested", "!sibling", "!siblingCommit", "!autonomous", "!dialog");
    }
}
