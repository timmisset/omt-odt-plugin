package com.misset.opp.omt.psi.prefixes;

import org.jetbrains.yaml.psi.YAMLKeyValue;

public interface OMTPrefix extends YAMLKeyValue {
    boolean isPrefix(String prefix);
}
