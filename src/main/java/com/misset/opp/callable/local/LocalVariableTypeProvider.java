package com.misset.opp.callable.local;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface LocalVariableTypeProvider {
    boolean providesTypeFor(String name);

    Set<OntResource> getType(String name,
                             Call call);
}
