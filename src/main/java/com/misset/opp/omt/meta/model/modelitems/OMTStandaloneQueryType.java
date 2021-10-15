package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.arrays.OMTParamsArrayType;
import com.misset.opp.omt.meta.markers.OMTVariableProvider;
import com.misset.opp.omt.meta.model.OMTGraphSelectionType;
import com.misset.opp.omt.meta.model.OMTPrefixesType;
import com.misset.opp.omt.meta.model.scalars.ODTQueryType;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameType;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static com.misset.opp.util.Collection.addToGroupedMap;

public class OMTStandaloneQueryType extends OMTMetaType implements OMTVariableProvider {
    protected OMTStandaloneQueryType() {
        super("OMT Component");
    }

    private static final HashMap<String, Supplier<YamlMetaType>> features = new HashMap<>();
    static {
        features.put("base", OMTVariableNameType::new);
        features.put("params", OMTParamsArrayType::new);
        features.put("graphs", OMTGraphSelectionType::new);
        features.put("prefixes", OMTPrefixesType::new);
        features.put("query", ODTQueryType::new);
    }

    @Override
    protected HashMap<String, Supplier<YamlMetaType>> getFeatures() {
        return features;
    }

    @Override
    public HashMap<String, List<YAMLPsiElement>> getVariableMap(YAMLMapping mapping,
                                                                ProblemsHolder holder) {
        HashMap<String, List<YAMLPsiElement>> variableMap = new HashMap<>();
        addSequenceToMap(mapping, "params", variableMap);

        final YAMLKeyValue base = mapping.getKeyValueByKey("base");
        if(base != null) {
            // base should adhere to the OMTVariableNameType otherwise it will throw an error on the syntax check
            addToGroupedMap(base.getValueText(), base, variableMap);
        }

        return variableMap;
    }
}
