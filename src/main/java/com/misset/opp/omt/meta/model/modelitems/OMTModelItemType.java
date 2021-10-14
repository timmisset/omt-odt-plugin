package com.misset.opp.omt.meta.model.modelitems;

import com.misset.opp.omt.meta.OMTMetaTaggedType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.HashMap;
import java.util.function.Supplier;

/**
 * The OMTModelItem are always typed by a Yaml Tag (!Tag)
 * This class serves as an intermediary that will defer validations and completions to delegates created based on the tag identifiers
 */
public class OMTModelItemType extends OMTMetaTaggedType {

    private static final HashMap<String, Supplier<YamlMetaType>> taggedTypes = new HashMap<>();
    static {
        taggedTypes.put("!Activity", OMTActivityType::new);
        taggedTypes.put("!Component", OMTComponentType::new);
        taggedTypes.put("!Ontology", OMTOntologyType::new);
    }

    public OMTModelItemType(@NonNls @NotNull String name) {
        super(name);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getTaggedTypes() {
        return taggedTypes;
    }
}
