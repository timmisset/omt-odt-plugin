package com.misset.opp.omt.psi.wrapper;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTModelItemType;
import com.misset.opp.omt.psi.impl.model.modelitems.OMTActivity;
import com.misset.opp.omt.psi.impl.model.modelitems.OMTComponent;
import com.misset.opp.omt.psi.impl.model.modelitems.OMTEmptyModelItem;
import com.misset.opp.omt.psi.impl.model.modelitems.OMTOntology;
import com.misset.opp.omt.psi.impl.model.modelitems.OMTProcedure;
import com.misset.opp.omt.psi.impl.model.modelitems.OMTStandaloneQuery;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import com.misset.opp.omt.util.OMTYamlUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class OMTModelItemFactory extends OMTElementFactory {

    private OMTModelItemFactory() {
    }

    public static OMTModelItem fromKeyValue(@NotNull YAMLKeyValue keyValue) {
        final ASTNode node = keyValue.getNode();

        if (isWrapped(node)) {
            return getWrapper(node, OMTModelItem.class);
        }
        if (keyValue.getValue() == null) {
            // an entry without any content, could still contain a Tag
            return wrap(node, new OMTEmptyModelItem(node));
        } else {
            return wrap(node, getModelItemFromValue(keyValue));
        }
    }

    private static OMTModelItem getModelItemFromValue(@NotNull YAMLKeyValue keyValue) {
        final ASTNode node = Objects.requireNonNull(keyValue.getValue())
                .getNode();
        switch (getType(keyValue)) {
            case Activity:
                return new OMTActivity(node);
            case Component:
                return new OMTComponent(node);
            case Ontology:
                return new OMTOntology(node);
            case Procedure:
                return new OMTProcedure(node);
            case StandaloneQuery:
                return new OMTStandaloneQuery(node);
            default:
                return null;
        }
    }

    private static OMTModelItemType getType(@NotNull YAMLKeyValue keyValue) {
        return Optional.of(keyValue)
                .map(OMTYamlUtil::getTagIdentifier)
                .filter(s -> Arrays.stream(OMTModelItemType.values())
                        .anyMatch(
                                omtModelItemType -> omtModelItemType.name()
                                        .equals(s)
                        ))
                .map(OMTModelItemType::valueOf)
                .orElse(OMTModelItemType.Unknown);
    }

}
