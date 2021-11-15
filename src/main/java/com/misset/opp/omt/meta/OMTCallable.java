package com.misset.opp.omt.meta;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Set;

/**
 * The OMTCallable interface can be implemented on OMTMetaType classes
 * These should be able to resolve a call made to them with a set of parameters
 */
public interface OMTCallable {

    /**
     * Resolve the OMTCallable, types are asserted from annotations on the structure itself
     */
    Set<OntResource> resolve(YAMLMapping mapping,
                             Set<OntResource> resources,
                             Call call);

}
