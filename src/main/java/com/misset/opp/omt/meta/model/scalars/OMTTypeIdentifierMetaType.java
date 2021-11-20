package com.misset.opp.omt.meta.model.scalars;

import com.misset.opp.omt.meta.ODTInjectable;
import com.misset.opp.omt.meta.model.ODTSimpleInjectable;
import org.jetbrains.yaml.meta.model.YamlScalarType;

/**
 * Used to identify a type such as a primitive or a Turtle class
 */
@ODTSimpleInjectable
public class OMTTypeIdentifierMetaType extends YamlScalarType implements ODTInjectable {
    public OMTTypeIdentifierMetaType() {
        super("OMT Type Identifier");
    }
}
