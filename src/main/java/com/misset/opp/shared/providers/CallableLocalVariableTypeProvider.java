package com.misset.opp.shared.providers;

import com.misset.opp.resolvable.local.LocalVariable;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CallableLocalVariableTypeProvider {
    /**
     * Returns an empty Set when the name doesn't match any local variable for this Callable
     * Returns a set of owl:Thing when the name matches a local variable but the type cannot
     * be resolved from the context
     */
    default Set<OntResource> resolve(String name,
                                     PsiCall call,
                                     int argumentIndex) {
        return Optional.ofNullable(getLocalVariable(name, call, argumentIndex))
                .map(LocalVariable::resolve)
                .map(resources -> resources.isEmpty() ? Set.of((OntResource) OppModel.INSTANCE.OWL_THING_INSTANCE) : resources)
                .orElse(Collections.emptySet());
    }

    default LocalVariable getLocalVariable(String name, PsiCall call, int argumentIndex) {
        return getLocalVariables(call, argumentIndex)
                .stream()
                .filter(localVariable -> localVariable.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    List<LocalVariable> getLocalVariables(PsiCall call, int argumentIndex);
}
