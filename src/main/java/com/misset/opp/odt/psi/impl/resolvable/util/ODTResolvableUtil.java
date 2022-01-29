package com.misset.opp.odt.psi.impl.resolvable.util;

import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ODTResolvableUtil {

    public static String getDocumentation(Set<OntResource> resources) {
        return getDocumentation(resources, true);
    }

    public static String getDocumentation(Set<OntResource> resources, boolean asHtmlList) {
        String prefix = asHtmlList ? "<ul><li>" : "";
        String suffix = asHtmlList ? "</li></ul>" : "";
        String joining = asHtmlList ? "</li><li>" : "<br>";
        if (resources.isEmpty()) {
            return "Unknown";
        }
        return prefix + resources.stream()
                .sorted(Comparator.comparing(Resource::toString))
                .map(TTLResourceUtil::describeUri)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(joining)) + suffix;
    }

}
