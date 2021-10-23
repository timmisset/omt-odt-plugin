package com.misset.opp.omt.meta.model.scalars;

import org.jetbrains.yaml.meta.model.YamlScalarType;

/**
 * Used to identify a type such as a primitive or a Turtle class
 */
public class OMTTypeIdentifierMetaType extends YamlScalarType {
    public OMTTypeIdentifierMetaType() {
        super("OMT Type Identifier");
    }
}
