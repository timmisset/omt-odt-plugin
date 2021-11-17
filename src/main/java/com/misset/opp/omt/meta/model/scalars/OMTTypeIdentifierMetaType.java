package com.misset.opp.omt.meta.model.scalars;

import com.misset.opp.omt.meta.ODTInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

/**
 * Used to identify a type such as a primitive or a Turtle class
 */
public class OMTTypeIdentifierMetaType extends YamlScalarType implements ODTInjectable {
    public OMTTypeIdentifierMetaType() {
        super("OMT Type Identifier");
    }
}
