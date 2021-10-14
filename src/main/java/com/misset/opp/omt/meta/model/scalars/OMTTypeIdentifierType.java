package com.misset.opp.omt.meta.model.scalars;

import org.jetbrains.yaml.meta.model.YamlScalarType;

/**
 * Used to identify a type such as a primitive or a Turtle class
 */
public class OMTTypeIdentifierType extends YamlScalarType {
    public OMTTypeIdentifierType() {
        super("OMT Type Identifier");
    }
}
