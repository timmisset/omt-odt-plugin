package com.misset.opp.omt.meta.model.modelitems;

import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaTaggedType;
import com.misset.opp.omt.meta.model.modelitems.ontology.OMTOntologyType;
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
public class OMTModelItemType extends OMTMetaTaggedType<OMTModelItemDelegate> {

    private static final HashMap<String, Supplier<OMTModelItemDelegate>> taggedTypes = new HashMap<>();
    static {
        taggedTypes.put("!Activity", OMTActivityType::new);
        taggedTypes.put("!Component", OMTComponentType::new);
        taggedTypes.put("!Ontology", OMTOntologyType::new);
        taggedTypes.put("!Procedure", OMTProcedureType::new);
        taggedTypes.put("!StandaloneQuery", OMTStandaloneQueryType::new);
    }

    public OMTModelItemType(@NonNls @NotNull String name) {
        super(name);
    }

    @Override
    protected HashMap<String, Supplier<OMTModelItemDelegate>> getTaggedTypes() {
        return taggedTypes;
    }

    public boolean isCallable(YAMLMapping mapping) {
        return Optional.of(mapping)
                .map(YAMLValue::getTag)
                .map(PsiElement::getText)
                .map(this::getDelegateByTag)
                .map(OMTModelItemDelegate::isCallable)
                .orElse(false);
    }
}
