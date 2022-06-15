package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.documentation.DocumentationMarkup;
import com.misset.opp.documentation.DocumentationProvider;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ODTResolvableQualifiedUriStepDocumentationUtil {

    private static final Pattern pattern = Pattern.compile("\"(.*)\"");

    public static String getDocumentation(ODTResolvableQualifiedUriStep step) {
        if (isClassUri(step)) {
            return getClassDocumentation(step);
        } else if (isIndividualUri(step)) {
            return getIndividualDocumentation(step);
        } else {
            return getTraverseDocumentation(step);
        }
    }

    private static String getIndividualDocumentation(ODTResolvableQualifiedUriStep step) {
        String fullyQualifiedUri = step.getFullyQualifiedUri();
        @Nullable Individual individual = OppModel.INSTANCE.getIndividual(fullyQualifiedUri);
        if (individual == null) {
            return null;
        }
        OntClass ontClass = OppModel.INSTANCE.toClass(individual);

        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append("Instance<br>");
        sb.append(fullyQualifiedUri);
        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        sb.append("An instance of " + ontClass.getURI());
        sb.append(DocumentationMarkup.CONTENT_END);

        sb.append(DocumentationMarkup.SECTIONS_START);
        setClassInfo(ontClass, sb);
        setPredicateInfo(individual, "Values", sb);
        sb.append(DocumentationMarkup.SECTIONS_END);
        return sb.toString();
    }

    private static String getClassDocumentation(ODTResolvableQualifiedUriStep step) {
        String fullyQualifiedUri = step.getFullyQualifiedUri();
        OntClass ontClass = OppModel.INSTANCE.getClass(fullyQualifiedUri);
        if (ontClass == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append("Class<br>");
        sb.append(fullyQualifiedUri);
        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        sb.append("An owl:Class in the OPP Ontology");
        sb.append(DocumentationMarkup.CONTENT_END);

        sb.append(DocumentationMarkup.SECTIONS_START);
        setClassInfo(ontClass, sb);
        Set<? extends OntResource> instances = OppModel.INSTANCE.toIndividuals(ontClass).stream()
                .filter(resource -> resource.getURI() != null && !resource.getURI().endsWith("_INSTANCE"))
                .collect(Collectors.toSet());
        if (!instances.isEmpty()) {
            DocumentationProvider.addKeyValueSection("Instances:",
                    TTLResourceUtil.describeUrisJoined(instances, "<br>", false)
                    , sb);
        }
        sb.append(DocumentationMarkup.SECTIONS_END);
        return sb.toString();
    }

    private static void setPredicateInfo(OntResource resource, String header, StringBuilder sb) {
        Set<Property> properties = getProperties(resource);
        if (properties.isEmpty()) {
            return;
        }
        StringBuilder predicatesTable = new StringBuilder();
        predicatesTable.append("<table>");
        boolean hasValues = false;
        for (Property property : properties) {
            if (!property.equals(OppModel.INSTANCE.RDF_TYPE)) {
                RDFNode propertyValue = resource.getPropertyValue(property);
                if (propertyValue == null && resource instanceof OntClass) {
                    propertyValue = getFromSuperclass((OntClass) resource, property);
                }
                if (propertyValue != null) {
                    String value = null;
                    if (propertyValue.isLiteral()) {
                        value = propertyValue.asLiteral().getValue().toString();
                    } else if (propertyValue.isResource()) {
                        value = propertyValue.asResource().getLocalName();
                    }
                    if (value != null) {
                        hasValues = true;
                        predicatesTable.append("<tr><td style=\"padding-right: 5px\">")
                                .append(property.getLocalName())
                                .append("</td><td>")
                                .append(extractValue(value))
                                .append("</td></tr>");
                    }
                }
            }
        }
        predicatesTable.append("</table>");
        if (hasValues) {
            DocumentationProvider.addKeyValueSection(header, predicatesTable.toString(), sb);
        }
    }

    private static RDFNode getFromSuperclass(OntClass ontClass, Property property) {
        return OppModel.INSTANCE.listSuperClasses(ontClass)
                .stream()
                .map(superClass -> superClass.getPropertyValue(property))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private static String extractValue(String fullyQualifiedLiteral) {
        Matcher matcher = pattern.matcher(fullyQualifiedLiteral);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return fullyQualifiedLiteral;
        }
    }

    private static Set<Property> getProperties(OntResource resource) {
        return OppModel.INSTANCE.listPredicates(OppModel.INSTANCE.toClass(resource))
                .stream()
                .filter(property -> !OppModel.INSTANCE.classModelProperties.contains(property))
                .collect(Collectors.toSet());
    }

    private static void setClassInfo(OntClass ontClass, StringBuilder sb) {
        // the order is important for super and subclasses, use list instead of set
        Set<OntClass> superClasses = OppModel.INSTANCE.listSuperClasses(ontClass);
        if (!superClasses.isEmpty()) {
            DocumentationProvider.addKeyValueSection("Superclasses:",
                    superClasses.stream().map(uri -> TTLResourceUtil.describeUri(uri, false))
                            .distinct()
                            .collect(Collectors.joining("<br>"))
                    , sb);
        }

        Set<OntClass> subClasses = OppModel.INSTANCE.listSubclasses(ontClass);
        if (!subClasses.isEmpty()) {
            DocumentationProvider.addKeyValueSection("Subclasses:",
                    subClasses.stream().map(uri -> TTLResourceUtil.describeUri(uri, false))
                            .distinct()
                            .collect(Collectors.joining("<br>"))
                    , sb);
        }

        setPredicateInfo(ontClass, "Predicates", sb);
    }

    private static String getTraverseDocumentation(ODTResolvableQualifiedUriStep step) {
        boolean isReversed = step.getParent() instanceof ODTQueryReverseStep;
        Set<OntResource> unfiltered = isReversed ? ((ODTResolvableQueryStep) step.getParent()).resolve() : step.resolve();
        Set<OntResource> filtered = step.getResolvableParent().filter(unfiltered);
        String fullyQualifiedUri = step.getFullyQualifiedUri();
        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append("Predicate<br>");
        sb.append(fullyQualifiedUri);
        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        final String content;
        if (isReversed) {
            content = "When traversing the model <u>reversed</u>, the query will return anything in the TTL ontology which " +
                    "has the predicate (sh:path) and has the previous query step as object (sh:dataType / sh:class)";
        } else {
            content = "When traversing the model, the query will return the object (sh:dataType / sh:class) of the " +
                    "previous query step (subject) using the predicate (sh:path)";
        }
        sb.append(content);
        sb.append(DocumentationMarkup.CONTENT_END);
        sb.append(DocumentationMarkup.SECTIONS_START);

        DocumentationProvider.addKeyValueSection("Direction:", isReversed ? "Reverse" : "Forward", sb);

        if (OppModel.INSTANCE.getProperty(fullyQualifiedUri) != null) {
            String label = isReversed ? "Subject(s)" : "Object(s)";
            DocumentationProvider.addKeyValueSection("Result", "<u>" + label + "</u><br>" + TTLResourceUtil.describeUrisJoined(filtered, "<br>", false), sb);
            if (unfiltered.size() != filtered.size()) {
                DocumentationProvider.addKeyValueSection("Unfiltered:", TTLResourceUtil.describeUrisJoined(unfiltered, "<br>", false), sb);
            }
        }

        Set<OntResource> previousStep = step.resolvePreviousStep();
        if (!previousStep.isEmpty()) {
            String label = isReversed ? "Object(s)" : "Subject(s)";
            DocumentationProvider.addKeyValueSection("Previous step", "<u>" + label + "</u><br>" + TTLResourceUtil.describeUrisJoined(previousStep, "<br>", false), sb);
        }


        sb.append(DocumentationMarkup.SECTIONS_END);
        return sb.toString();
    }

    private static boolean isClassUri(ODTResolvableQualifiedUriStep step) {
        return OppModel.INSTANCE.getClass(step.getFullyQualifiedUri()) != null;
    }

    private static boolean isIndividualUri(ODTResolvableQualifiedUriStep step) {
        return OppModel.INSTANCE.getIndividual(step.getFullyQualifiedUri()) != null;
    }

}
