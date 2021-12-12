package com.misset.opp.callable.local;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface CallableLocalVariableTypeProvider {
    default Set<OntResource> getType(String name,
                                     Call call,
                                     int argumentIndex) {
        return getLocalVariables(call, argumentIndex)
                .stream()
                .filter(localVariable -> localVariable.getName().equals(name))
                .map(LocalVariable::getType)
                .findFirst()
                .orElse(Collections.emptySet());
    }

    List<LocalVariable> getLocalVariables(Call call, int argumentIndex);
}
