package com.misset.opp.omt.psi;

import org.apache.jena.rdf.model.Resource;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

import java.util.List;

public interface OMTVariable extends YAMLSequenceItem {

    String READ_ONLY_KEY = "readOnly";
    String NAME_KEY = "name";
    String DEFAULT_VALUE_KEY = "value";

    /**
     * The name of the variable (without the $ prefix)
     */
    String getName();

    /**
     * Whether this variable has been marked as read-only
     */
    boolean isReadOnly();

    /**
     * The default value set for a Variable
     */
    List<Resource> getDefaultValue();



}
