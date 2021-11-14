package com.misset.opp.omt.meta;

import com.misset.opp.callable.Call;
import com.misset.opp.callable.Callable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Set;

/**
 * The OMTCallable interface can be implemented on OMTMetaType classes
 * These should be able to resolve a call made to them with a set of parameters
 */
public interface OMTCallable extends Callable {

    /**
     * Resolve the OMTCallable with the set of resources going into the call and the call itself
     */
    Set<OntResource> resolve(YAMLMapping mapping,
                             Set<OntResource> resources,
                             Call call);

}
