package com.misset.opp.ttl.util;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.misset.opp.odt.completion.ODTTraverseCompletion;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.util.Icons;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;

import java.util.List;
import java.util.Map;
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

    public static String fromIriRef(String iriRef) {
        if (iriRef.startsWith("<") && iriRef.endsWith(">")) {
            return iriRef.substring(1, iriRef.length() - 1);
        }
        return iriRef;
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


    public static LookupElementBuilder getPredicateLookupElement(Resource resource,
                                                                 ODTTraverseCompletion.TraverseDirection direction,
                                                                 Map<String, String> availableNamespaces) {
        String title = ODTTraverseCompletion.parseToCurie(resource, availableNamespaces);
        if (title == null) {
            return null;
        }
        String lookupText = direction == ODTTraverseCompletion.TraverseDirection.REVERSE ? "^" + title : title;
        return LookupElementBuilder.create(lookupText)
                .withLookupStrings(Set.of(resource.getURI(), resource.getLocalName()))
                .withTailText(direction == ODTTraverseCompletion.TraverseDirection.FORWARD ? " -> forward" : " <- reverse")
                .withTypeText("", Icons.TTLFile, false)
                .withIcon(Icons.TTLFile)
                .withPresentableText(title);
    }

    public static LookupElementBuilder getRootLookupElement(Resource resource,
                                                            String typeText,
                                                            Map<String, String> availableNamespaces) {
        String title = ODTTraverseCompletion.parseToCurie(resource, availableNamespaces);
        if (title == null) {
            return null;
        }
        String lookupText = "/" + title;
        return LookupElementBuilder.create(lookupText)
                .withLookupStrings(Set.of(resource.getURI(), resource.getLocalName()))
                .withTypeText(typeText, Icons.TTLFile, false)
                .withIcon(Icons.TTLFile)
                .withPresentableText(title);
    }

    public static LookupElementBuilder getTypeLookupElement(OntResource resource,
                                                            Map<String, String> availableNamespaces) {
        String lookupText = ODTTraverseCompletion.parseToCurie(resource, availableNamespaces);
        if (lookupText == null) {
            return null;
        }
        String typeText = isType(resource) ? "Type" : "Class";
        return LookupElementBuilder.create(lookupText)
                .withLookupStrings(Set.of(resource.getURI(), resource.getLocalName()))
                .withTypeText("Type", Icons.TTLFile, false)
                .withIcon(Icons.TTLFile)
                .withPresentableText(lookupText);
    }
}
