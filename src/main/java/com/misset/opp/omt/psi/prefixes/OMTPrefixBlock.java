package com.misset.opp.omt.psi.prefixes;

import org.jetbrains.yaml.psi.YAMLKeyValue;

import java.util.List;

public interface OMTPrefixBlock extends YAMLKeyValue {
    List<OMTPrefix> getPrefixes();

    OMTPrefix getPrefix(String prefix);

    boolean hasPrefix(String prefix);
}
