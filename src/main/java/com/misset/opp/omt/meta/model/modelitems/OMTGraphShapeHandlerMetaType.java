package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.documentation.OMTDocumented;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.arrays.OMTHandlersArrayMetaType;
import com.misset.opp.omt.meta.scalars.queries.OMTShapeQueryType;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.Context;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

/**
 * An OMTGraphShapeHandler is callable but never has any parameters. The OMTParameterizedModelItemMetaType will handle
 * this to make sure it is considered a Callable with min/max parameters = 0.
 */
public class OMTGraphShapeHandlerMetaType extends OMTParameterizedModelItemMetaType implements
        OMTMetaCallable,
        OMTDocumented {
    private static final OMTGraphShapeHandlerMetaType INSTANCE = new OMTGraphShapeHandlerMetaType();

    public static OMTGraphShapeHandlerMetaType getInstance() {
        return INSTANCE;
    }

    private OMTGraphShapeHandlerMetaType() {
        super("OMT GraphShape Handler");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();

    static {
        features.put("id", YamlStringType::getInstance);
        features.put("shape", OMTShapeQueryType::getInstance);
        features.put("handlers", OMTHandlersArrayMetaType::getInstance);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public boolean isCallable() {
        return true;
    }

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping, Context context) {
        return Collections.emptySet();
    }

    @Override
    public boolean isVoid(YAMLMapping mapping) {
        return true;
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
    public CallableType getType() {
        return CallableType.GRAPH_SHAPE_HANDLER;
    }

    @Override
    public String getDocumentationClass() {
        return "GraphShapeHandler";
    }
}
