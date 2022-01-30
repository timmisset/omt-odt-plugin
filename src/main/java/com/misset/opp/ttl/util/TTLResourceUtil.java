package com.misset.opp.ttl.util;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.misset.opp.odt.completion.ODTTraverseCompletion;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.util.Icons;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TTLResourceUtil {
    private static final HashMap<OntResource, String> descriptions = new HashMap<>();
    private static final HashMap<OntResource, String> descriptionsWithType = new HashMap<>();
    private static final HashMap<OntResource, Boolean> isType = new HashMap<>();
    private static final HashMap<OntResource, Boolean> isValue = new HashMap<>();
    private static final HashMap<OntResource, Boolean> isXSDType = new HashMap<>();
    private static final HashMap<OntResource, Boolean> isClass = new HashMap<>();
    private static final HashMap<OntResource, Boolean> isIndividual = new HashMap<>();

    private static boolean is(OntResource resource, HashMap<OntResource, Boolean> cache, Supplier<Boolean> orElse) {
        if (cache.containsKey(resource)) {
            return cache.get(resource);
        }
        Boolean result = orElse.get();
        cache.put(resource, result);
        return result;
    }

    public static boolean isType(OntResource resource) {
        return is(resource, isType, () -> resource.isClass() && isXSDType(resource));
    }

    public static boolean isValue(OntResource resource) {
        return is(resource, isValue, () -> resource instanceof Individual && isXSDType(OppModel.INSTANCE.toClass(resource)));
    }

    public static boolean isXSDType(OntResource resource) {
        return is(resource, isXSDType, () -> resource.getNameSpace().equals(OppModel.XSD));
    }

    public static boolean isClass(OntResource resource) {
        return is(resource, isClass, () -> !isType(resource) && resource.isClass());
    }

    public static boolean isIndividual(OntResource resource) {
        return is(resource, isIndividual, resource::isIndividual);
    }

    public static String describeUrisJoined(Set<OntResource> resources) {
        return describeUrisJoined(resources, ", ", true);
    }

    public static String describeUrisJoined(Set<? extends OntResource> resources, String delimiter) {
        return String.join(delimiter, describeUris(resources, true));
    }

    public static String describeUrisJoined(Set<? extends OntResource> resources, String delimiter, boolean withType) {
        return String.join(delimiter, describeUris(resources, withType));
    }

    public static List<String> describeUris(Set<? extends OntResource> resources, boolean withType) {
        return resources.stream()
                .map(resource -> describeUri(resource, withType))
                .sorted()
                .collect(Collectors.toList());
    }

    public static String describeUri(OntResource resource) {
        return describeUri(resource, true);
    }

    public static String describeUri(OntResource resource, boolean withType) {
        HashMap<OntResource, String> cache = withType ? descriptionsWithType : descriptions;
        if (cache.containsKey(resource)) {
            return cache.get(resource);
        }
        String describedUri = doDescribeUri(resource, withType);
        cache.put(resource, describedUri);
        return describedUri;
    }

    private static String doDescribeUri(OntResource resource, boolean withType) {
        if (resource.isClass()) {
            if (resource.getNameSpace().equals(OppModel.XSD)) {
                return resource.getURI() + (withType ? " (TYPE)" : "");
            }
            return resource.getURI() + (withType ? " (CLASS)" : "");
        } else if (resource instanceof Individual) {
            final Individual individual = (Individual) resource;
            OntClass ontClass = OppModel.INSTANCE.toClass(individual);
            if (ontClass == null) {
                return null;
            }
            if (isXSDType(ontClass)) {
                return ontClass.getURI() + (withType ? " (VALUE)" : "");
            } else if (ontClass.equals(OppModel.INSTANCE.OPP_CLASS)) {
                // Specific OPP_CLASS instances that describe non-ontology values such as JSON_OBJECT, ERROR etc
                return individual.getURI();
            } else if (individual.getNameSpace() != null &&
                    individual.getLocalName() != null &&
                    !individual.getLocalName().endsWith("_INSTANCE")) {
                return individual.getURI();
            }
            return ontClass.getURI() + (withType ? " (INSTANCE)" : "");
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


    public static String describeUrisForLookupJoined(Set<? extends OntResource> resources) {
        return describeUrisForLookupJoined(resources, ", ");
    }

    public static String describeUrisForLookupJoined(Set<? extends OntResource> resources, String delimiter) {
        return String.join(delimiter, describeUrisLookup(resources));
    }

    public static List<String> describeUrisLookup(Set<? extends OntResource> resources) {
        return resources.stream()
                .map(TTLResourceUtil::describeUriForLookup)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public static String describeUriForLookup(OntResource resource) {
        return Optional.ofNullable(OppModel.INSTANCE.toClass(resource))
                .map(ontClass -> ontClass.equals(OppModel.INSTANCE.OPP_CLASS) ? resource : ontClass)
                .map(Resource::getLocalName)
                .orElse("Ontology class could not be found in the model");
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
                .withTypeText(typeText, Icons.TTLFile, false)
                .withIcon(Icons.TTLFile)
                .withPresentableText(lookupText);
    }
}
