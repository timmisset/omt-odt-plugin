package com.misset.opp.omt.meta.providers.util;

import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQuery;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.omt.meta.providers.OMTLocalVariableTypeProvider;
import com.misset.opp.resolvable.Resolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class OMTVariableTypeProviderUtil {

    private OMTVariableTypeProviderUtil() {
        // empty constructor
    }

    public static Set<OntResource> getType(OMTLocalVariableTypeProvider localVariableTypeProvider,
                                           YAMLMapping mapping) {
        final YAMLValue yamlValue = localVariableTypeProvider.getTypeProviderMap(mapping);
        return Optional.ofNullable(yamlValue)
                .map(value -> OMTODTInjectionUtil.getInjectedContent(value, ODTResolvableQuery.class))
                .orElse(Collections.emptySet())
                .stream()
                .map(Resolvable::resolve)
                .findFirst()
                .orElse(Collections.emptySet());
    }
}
