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
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Helper class that is particularly to describe resources for documentation, completion and error/warning messages
 * Not used for validation, resolving etc.
 */
public class TTLResourceUtil {
    private static final HashMap<Resource, String> descriptions = new HashMap<>();
    private static final HashMap<Resource, String> descriptionsWithType = new HashMap<>();
    private static final HashMap<Resource, Boolean> isType = new HashMap<>();
    private static final HashMap<Resource, Boolean> isXSDType = new HashMap<>();

    private static boolean is(Resource resource, HashMap<Resource, Boolean> cache, Function<Resource, Boolean> orElse) {
        if (cache.containsKey(resource)) {
            return cache.get(resource);
        }
        Boolean result = orElse.apply(resource);
        cache.put(resource, result);
        return result;
    }

    public static boolean isType(Resource resource) {
        return is(resource, isType, _resource ->
                _resource instanceof OntResource &&
                        OppModel.INSTANCE.isClass((OntResource) _resource) && isXSDType(_resource));
    }

    public static boolean isXSDType(Resource resource) {
        return is(resource, isXSDType, _resource -> _resource.getNameSpace().equals(OppModel.XSD));
    }

    public static String describeUrisJoined(Set<? extends Resource> resources) {
        return describeUrisJoined(resources, ", ", true);
    }

    public static String describeUrisJoined(Set<? extends Resource> resources, String delimiter, boolean withType) {
        return String.join(delimiter, describeUris(resources, withType));
    }

    public static List<String> describeUris(Set<? extends Resource> resources, boolean withType) {
        return resources.stream()
                .map(resource -> describeUri(resource, withType))
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());
    }

    public static String describeUri(Resource resource, boolean withType) {
        HashMap<Resource, String> cache = withType ? descriptionsWithType : descriptions;
        if (cache.containsKey(resource)) {
            return cache.get(resource);
        }
        String describedUri = doDescribeUri(resource, withType);
        cache.put(resource, describedUri);
        return describedUri;
    }

    private static String doDescribeUri(Resource resource, boolean withType) {
        if (resource instanceof OntResource && ((OntResource) resource).isClass()) {
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
                // Specific OPP_CLASS instances that describe non-ontology values such as ERROR etc
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
                                                                 Set<OntResource> objects,
                                                                 ODTTraverseCompletion.TraverseDirection direction,
                                                                 Map<String, String> availableNamespaces) {
        String title = ODTTraverseCompletion.parseToCurie(resource, availableNamespaces);
        if (title == null) {
            return null;
        }
        String lookupText = direction == ODTTraverseCompletion.TraverseDirection.REVERSE ? "^" + title : title;
        String typeText = "";
        if (!objects.isEmpty()) {
            typeText = TTLResourceUtil.describeUrisForLookupJoined(objects.stream().limit(2).collect(Collectors.toSet()));
            if (objects.size() > 2) {
                typeText += "...";
            }
        }
        return LookupElementBuilder.create(lookupText)
                .withLookupStrings(Set.of(resource.getURI(), resource.getLocalName()))
                .withTailText(direction == ODTTraverseCompletion.TraverseDirection.FORWARD ? " -> forward" : " <- reverse")
                .withTypeText(typeText)
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
                .withTypeText(typeText)
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
