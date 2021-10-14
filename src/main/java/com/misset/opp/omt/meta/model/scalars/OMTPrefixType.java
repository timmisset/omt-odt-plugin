package com.misset.opp.omt.meta.model.scalars;

import org.jetbrains.yaml.meta.model.YamlScalarType;

/**
 * A scalar value used to identify a prefix
 * For the entries in the prefixes: block, see OMTIriType
 */
public class OMTPrefixType extends YamlScalarType {
    protected OMTPrefixType() {
        super("OMT Prefix");
    }
}
