package com.misset.opp.model.util;

import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.OntologyModelTranslator;
import com.misset.opp.model.OntologyTraverseDirection;
import com.misset.opp.model.constants.XSD;
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
@Service
public final class OntologyResourceUtil {

    private static final HashMap<Resource, String> descriptions = new HashMap<>();
    private static final HashMap<Resource, String> descriptionsWithType = new HashMap<>();
    private static final HashMap<Resource, Boolean> isType = new HashMap<>();
    private static final HashMap<Resource, Boolean> isXSDType = new HashMap<>();
    private final OntologyModel ontologyModel;
    private final OntologyModelTranslator ontologyModelTranslator;

    public OntologyResourceUtil(@NotNull Project project) {
        ontologyModel = OntologyModel.getInstance(project);
        ontologyModelTranslator = OntologyModelTranslator.getInstance(project);
    }

    public static OntologyResourceUtil getInstance(@NotNull Project project) {
        return project.getService(OntologyResourceUtil.class);
    }

    public boolean isType(Resource resource) {
        return isType.computeIfAbsent(resource, r -> r instanceof OntResource &&
                ontologyModel.isClass((OntResource) r) && isXSDType(r));
    }

    public boolean isXSDType(Resource resource) {
        return isXSDType.computeIfAbsent(resource, r -> r.getNameSpace().equals(XSD.NAMESPACE));
    }

    public String describeUrisJoined(Set<? extends Resource> resources) {
        return describeUrisJoined(resources, ", ", true);
    }

    public String describeUrisJoined(Set<? extends Resource> resources, String delimiter, boolean withType) {
        return String.join(delimiter, describeUris(resources, withType));
    }

    public List<String> describeUris(Set<? extends Resource> resources, boolean withType) {
        return resources.stream()
                .map(resource -> describeUri(resource, withType))
                .filter(Objects::nonNull)
                .sorted()
                .collect(Collectors.toList());
    }

    public String describeUri(Resource resource, boolean withType) {
        HashMap<Resource, String> cache = withType ? descriptionsWithType : descriptions;
        if (cache.containsKey(resource)) {
            return cache.get(resource);
        }
        String describedUri = doDescribeUri(resource, withType);
        cache.put(resource, describedUri);
        return describedUri;
    }

    private String doDescribeUri(Resource resource, boolean withType) {
        if (resource instanceof OntResource && ((OntResource) resource).isClass()) {
            return describeClass(resource, withType);
        } else if (resource instanceof Individual) {
            return describeIndividual((Individual) resource, withType);
        } else {
            return resource.getURI();
        }
    }

    @Nullable
    private String describeIndividual(Individual resource, boolean withType) {
        OntClass ontClass = ontologyModel.toClass(resource);
        if (ontClass == null) {
            return null;
        }
        if (isXSDType(ontClass)) {
            return ontClass.getURI() + (withType ? " (VALUE)" : "");
        } else if (ontClass.equals(OntologyModelConstants.getOppClass())) {
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
    private String describeClass(Resource resource, boolean withType) {
        if (resource.getNameSpace().equals(XSD.NAMESPACE)) {
            return resource.getURI() + (withType ? " (TYPE)" : "");
        }
        return resource.getURI() + (withType ? " (CLASS)" : "");
    }

    public String describeUrisForLookupJoined(Set<? extends OntResource> resources) {
        return describeUrisForLookupJoined(resources, ", ");
    }

    public String describeUrisForLookupJoined(Set<? extends OntResource> resources, String delimiter) {
        return String.join(delimiter, describeUrisLookup(resources));
    }

    public List<String> describeUrisLookup(Set<? extends OntResource> resources) {
        return resources.stream()
                .map(this::describeUriForLookup)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public String describeUriForLookup(OntResource resource) {
        return Optional.ofNullable(ontologyModel.toClass(resource))
                .map(ontClass -> ontClass.equals(OntologyModelConstants.getOppClass()) ? resource : ontClass)
                .map(Resource::getLocalName)
                .orElse("Ontology class could not be found in the model");
    }

    public String getCardinalityLabel(Set<OntResource> resources, Property property) {
        if (property == null) {
            return null;
        }
        if (resources.isEmpty()) {
            OntResource unambigiousResource = ontologyModel.getUnambigiousResource(property);
            if (unambigiousResource == null) {
                return null;
            }
            resources = Set.of(unambigiousResource);
        }
        boolean isMultiple = ontologyModelTranslator.isMultiple(resources, property);
        boolean isSingle = ontologyModelTranslator.isSingleton(resources, property);
        boolean isRequired = ontologyModelTranslator.isRequired(resources, property);
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

    public LookupElementBuilder getPredicateLookupElement(Set<OntResource> subjects,
                                                          Property property,
                                                          Set<OntResource> objects,
                                                          OntologyTraverseDirection.TraverseDirection direction,
                                                          String title) {
        if (title == null || property == null) {
            return null;
        }
        String cardinality = direction == OntologyTraverseDirection.TraverseDirection.FORWARD ? getCardinalityLabel(subjects, property) : getCardinalityLabel(objects, property);
        if (cardinality == null) {
            cardinality = "";
        }
        String typeText = "";
        if (!objects.isEmpty()) {
            typeText = describeUrisForLookupJoined(objects.stream().limit(2).collect(Collectors.toSet()));
            if (objects.size() > 2) {
                typeText += "...";
            }
        }
        return LookupElementBuilder.create(title)
                .withLookupStrings(Set.of(property.getURI(), property.getLocalName()))
                .withTailText((direction == OntologyTraverseDirection.TraverseDirection.FORWARD ? " -> forward " : " <- reverse ") + cardinality)
                .withTypeText(typeText)
                .withIcon(Icons.TTLFile)
                .withPresentableText(title);
    }

    public LookupElementBuilder getRootLookupElement(Resource resource,
                                                     String typeText,
                                                     Map<String, String> availableNamespaces) {
        String title = parseToCurie(resource, availableNamespaces);
        if (title == null) {
            return null;
        }
        String lookupText = "/" + title;
        return LookupElementBuilder.create(lookupText)
                .withLookupStrings(Set.of(resource.getURI(), resource.getLocalName()))
                .withTypeText(typeText)
                .withIcon(Icons.TTLFile);
    }

    public LookupElementBuilder getTypeLookupElement(OntResource resource,
                                                     Map<String, String> availableNamespaces) {
        String lookupText = parseToCurie(resource, availableNamespaces);
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

    public String parseToCurie(Resource resource,
                               Map<String, String> availableNamespaces) {
        return Optional.ofNullable(resource.getURI())
                .map(uri -> getCurie(resource, availableNamespaces, uri))
                .orElse(null);
    }

    private String getCurie(Resource resource,
                            Map<String, String> availableNamespaces,
                            String uri) {
        return availableNamespaces.containsKey(resource.getNameSpace()) ?
                (availableNamespaces.get(resource.getNameSpace()) + ":" + resource.getLocalName()) :
                "<" + uri + ">";
    }

}
