package com.misset.opp.ttl.util;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.misset.opp.odt.completion.ODTTraverseCompletion;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.model.OppModelTranslator;
import com.misset.opp.ttl.model.constants.XSD;
import com.misset.opp.util.Icons;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper class that is particularly to describe resources for documentation, completion and error/warning messages
 * Not used for validation, resolving etc.
 */
public class TTLResourceUtil {

    private TTLResourceUtil() {
        // empty constructor
    }

    private static final HashMap<Resource, String> descriptions = new HashMap<>();
    private static final HashMap<Resource, String> descriptionsWithType = new HashMap<>();
    private static final HashMap<Resource, Boolean> isType = new HashMap<>();
    private static final HashMap<Resource, Boolean> isXSDType = new HashMap<>();

    public static boolean isType(Resource resource) {
        return isType.computeIfAbsent(resource, r -> r instanceof OntResource &&
                OppModel.getInstance().isClass((OntResource) r) && isXSDType(r));
    }

    public static boolean isXSDType(Resource resource) {
        return isXSDType.computeIfAbsent(resource, r -> r.getNameSpace().equals(XSD.NAMESPACE));
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
            return describeClass(resource, withType);
        } else if (resource instanceof Individual) {
            return describeIndividual((Individual) resource, withType);
        } else {
            return resource.getURI();
        }
    }

    @Nullable
    private static String describeIndividual(Individual resource, boolean withType) {
        OntClass ontClass = OppModel.getInstance().toClass(resource);
        if (ontClass == null) {
            return null;
        }
        if (isXSDType(ontClass)) {
            return ontClass.getURI() + (withType ? " (VALUE)" : "");
        } else if (ontClass.equals(OppModelConstants.getOppClass())) {
            // Specific OPP_CLASS instances that describe non-ontology values such as ERROR etc
            return resource.getURI();
        } else if (resource.getNameSpace() != null &&
                resource.getLocalName() != null &&
                !resource.getLocalName().endsWith("_INSTANCE")) {
            return resource.getURI();
        }
        return ontClass.getURI() + (withType ? " (INSTANCE)" : "");
    }

    @NotNull
    private static String describeClass(Resource resource, boolean withType) {
        if (resource.getNameSpace().equals(XSD.NAMESPACE)) {
            return resource.getURI() + (withType ? " (TYPE)" : "");
        }
        return resource.getURI() + (withType ? " (CLASS)" : "");
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
        return Optional.ofNullable(OppModel.getInstance().toClass(resource))
                .map(ontClass -> ontClass.equals(OppModelConstants.getOppClass()) ? resource : ontClass)
                .map(Resource::getLocalName)
                .orElse("Ontology class could not be found in the model");
    }

    public static String getCardinalityLabel(Set<OntResource> resources, Property property) {
        if (property == null) {
            return null;
        }
        if (resources.isEmpty()) {
            OntResource unambigiousResource = OppModel.getInstance().getUnambigiousResource(property);
            if (unambigiousResource == null) {
                return null;
            }
            resources = Set.of(unambigiousResource);
        }
        boolean isMultiple = OppModelTranslator.isMultiple(resources, property);
        boolean isSingle = OppModelTranslator.isSingleton(resources, property);
        boolean isRequired = OppModelTranslator.isRequired(resources, property);
        if (isRequired) {
            if (isMultiple) {
                return "+";
            }
            if (isSingle) {
                return "1";
            }
        } else {
            if (isMultiple) {
                return "*";
            }
            if (isSingle) {
                return "?";
            }
        }
        return null;
    }

    public static LookupElementBuilder getPredicateLookupElement(Set<OntResource> subjects,
                                                                 Property property,
                                                                 Set<OntResource> objects,
                                                                 ODTTraverseCompletion.TraverseDirection direction,
                                                                 String title) {
        if (title == null || property == null) {
            return null;
        }
        String cardinality = direction == ODTTraverseCompletion.TraverseDirection.FORWARD ? getCardinalityLabel(subjects, property) : getCardinalityLabel(objects, property);
        if (cardinality == null) {
            cardinality = "";
        }
        String typeText = "";
        if (!objects.isEmpty()) {
            typeText = TTLResourceUtil.describeUrisForLookupJoined(objects.stream().limit(2).collect(Collectors.toSet()));
            if (objects.size() > 2) {
                typeText += "...";
            }
        }
        return LookupElementBuilder.create(title)
                .withLookupStrings(Set.of(property.getURI(), property.getLocalName()))
                .withTailText((direction == ODTTraverseCompletion.TraverseDirection.FORWARD ? " -> forward " : " <- reverse ") + cardinality)
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
