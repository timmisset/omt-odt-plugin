package com.misset.opp.ttl.util;

import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TTLResourceUtil {
    public static boolean isType(OntResource resource) {
        return resource.isClass() && isXSDType(resource);
    }

    public static boolean isValue(OntResource resource) {
        return resource instanceof Individual && isXSDType(((Individual) resource).getOntClass());
    }

    public static boolean isXSDType(OntResource resource) {
        return resource.getNameSpace().equals(OppModel.XSD);
    }

    public static boolean isClass(OntResource resource) {
        return !isType(resource) && resource.isClass();
    }

    public static String describeUrisJoined(Set<OntResource> resources) {
        return String.join(", ", describeUris(resources));
    }
    public static List<String> describeUris(Set<OntResource> resources) {
        return resources.stream()
                .map(TTLResourceUtil::describeUri)
                .sorted()
                .collect(Collectors.toList());
    }
    public static String describeUri(OntResource resource) {
        if (resource.isClass()) {
            if (resource.getNameSpace().equals(OppModel.XSD)) {
                return resource.getURI() + " (TYPE)";
            }
            return resource.getURI() + " (CLASS)";
        } else if (resource instanceof Individual) {
            final Individual individual = (Individual) resource;
            if (isXSDType(individual.getOntClass())) {
                return individual.getOntClass().getURI() + " (VALUE)";
            } else if (individual.getOntClass().equals(OppModel.INSTANCE.OPP_CLASS)) {
                // Specific OPP_CLASS instances that describe non-ontology values such as JSON_OBJECT, ERROR etc
                return individual.getURI();
            } else if (individual.getNameSpace() != null && !individual.getNameSpace().endsWith("_INSTANCE")) {
                return individual.getURI();
            }
            return individual.getOntClass().getURI() + " (INSTANCE)";
        } else {
            return resource.getURI();
        }
    }


    public static String describeUrisForLookupJoined(Set<OntResource> resources) {
        return String.join(", ", describeUrisLookup(resources));
    }

    public static List<String> describeUrisLookup(Set<OntResource> resources) {
        return resources.stream()
                .map(TTLResourceUtil::describeUriForLookup)
                .sorted()
                .collect(Collectors.toList());
    }

    public static String describeUriForLookup(OntResource resource) {
        if (resource instanceof Individual) {
            return ((Individual) resource).getOntClass().getLocalName();
        }
        return resource.getLocalName();
    }


}
