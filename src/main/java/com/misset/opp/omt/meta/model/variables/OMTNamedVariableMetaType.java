package com.misset.opp.omt.meta.model.variables;

import com.intellij.openapi.util.TextRange;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.Set;

public interface OMTNamedVariableMetaType {

    default Set<OntResource> getType(YAMLValue value) {
        return Collections.emptySet();
    }

    default Set<OntResource> getTypeFromDestructed(YAMLMapping mapping) {
        return Collections.emptySet();
    }

    String getName(YAMLValue value);

    default String getDescription(YAMLValue value) {
        return getName(value);
    }

    TextRange getNameTextRange(YAMLValue value);
}
