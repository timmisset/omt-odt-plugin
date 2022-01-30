package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.scalars.values.OMTLoadablePathMetaType;
import com.misset.opp.omt.meta.scalars.values.OMTLoadableSchemaMetaType;
import com.misset.opp.resolvable.Context;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class OMTLoadableMetaType extends OMTModelItemDelegateMetaType implements OMTMetaCallable {
    protected OMTLoadableMetaType() {
        super("OMT Loadable");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    private static final Set<String> required = Set.of("path", "schema");

    static {
        features.put("id", YamlStringType::new);
        features.put("path", OMTLoadablePathMetaType::new);
        features.put("schema", OMTLoadableSchemaMetaType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    protected Set<String> getRequiredFields() {
        return required;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping, Context context) {
        return Set.of(OppModel.INSTANCE.JSON_OBJECT);
    }

    @Override
    public boolean isVoid(YAMLMapping mapping) {
        return false;
    }

    @Override
    public boolean canBeAppliedTo(YAMLMapping mapping, Set<OntResource> resources) {
        return false;
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        return Collections.emptySet();
    }

    @Override
    public String getType() {
        return "Loadable";
    }
}
