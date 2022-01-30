package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaTaggedType;
import com.misset.opp.omt.meta.model.modelitems.ontology.OMTOntologyMetaType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * The OMTModelItem are always typed by a Yaml Tag (!Tag)
 * This class serves as an intermediary that will defer validations and completions to delegates created based on the tag identifiers
 */
public class OMTModelItemMetaType extends OMTMetaTaggedType<OMTModelItemDelegateMetaType> {

    private static final HashMap<String, Supplier<OMTModelItemDelegateMetaType>> taggedTypes = new HashMap<>();
    static {
        taggedTypes.put("!Activity", OMTActivityMetaType::new);
        taggedTypes.put("!Loadable", OMTLoadableMetaType::new);
        taggedTypes.put("!Component", OMTComponentMetaType::new);
        taggedTypes.put("!Ontology", OMTOntologyMetaType::new);
        taggedTypes.put("!Procedure", OMTProcedureMetaType::new);
        taggedTypes.put("!StandaloneQuery", OMTStandaloneQueryMetaType::new);
    }

    public OMTModelItemMetaType(@NonNls @NotNull String name) {
        super(name);
    }

    @Override
    protected HashMap<String, Supplier<OMTModelItemDelegateMetaType>> getTaggedTypes() {
        return taggedTypes;
    }

    public boolean isCallable(YAMLMapping mapping) {
        return Optional.ofNullable(mapping)
                .map(YAMLValue::getTag)
                .map(PsiElement::getText)
                .map(this::getDelegateByTag)
                .map(OMTModelItemDelegateMetaType::isCallable)
                .orElse(false);
    }

}
