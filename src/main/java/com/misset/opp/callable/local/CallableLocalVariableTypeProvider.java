package com.misset.opp.callable.local;

import com.misset.opp.callable.Call;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface CallableLocalVariableTypeProvider {
    /**
     * Returns an empty Set when the name doesn't match any local variable for this Callable
     * Returns a set of owl:Thing when the name matches a local variable but the type cannot
     * be resolved from the context
     */
    default Set<OntResource> getType(String name,
                                     Call call,
                                     int argumentIndex) {
        return getLocalVariables(call, argumentIndex)
                .stream()
                .filter(localVariable -> localVariable.getName().equals(name))
                .map(LocalVariable::getType)
                .map(resources -> resources.isEmpty() ? Set.of((OntResource) OppModel.INSTANCE.OWL_THING_INSTANCE) : resources)
                .findFirst()
                .orElse(Collections.emptySet());
    }

    List<LocalVariable> getLocalVariables(Call call, int argumentIndex);
}
