package com.misset.opp.omt.meta.scalars;

import com.misset.opp.omt.meta.OMTMetaInjectable;
import com.misset.opp.omt.meta.model.SimpleInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

/**
 * Used to identify a type such as a primitive or a Turtle class
 */
@SimpleInjectable
public class OMTTypeIdentifierMetaType extends YamlScalarType implements OMTMetaInjectable {
    public OMTTypeIdentifierMetaType() {
        super("OMT Type Identifier");
    }
}
