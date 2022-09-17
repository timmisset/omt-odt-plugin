package com.misset.opp.odt.documentation;

import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.psi.PsiElement;
import com.jgoodies.common.base.Strings;
import com.misset.opp.documentation.DocumentationProvider;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyResourceUtil;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.odt.psi.impl.ODTQueryOperationStepImpl;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.resolvable.querystep.ODTResolvableQualifiedUriStep;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ODTUriStepDocumentationUtil {

    private ODTUriStepDocumentationUtil() {
        // empty constructor
    }

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
        @Nullable Individual individual = OntologyModel.getInstance().getIndividual(fullyQualifiedUri);
        if (individual == null) {
            return null;
        }
        OntClass ontClass = OntologyModel.getInstance().toClass(individual);

        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append("Instance<br>");
        sb.append(fullyQualifiedUri);
        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        if (ontClass != null) {
            sb.append("An instance of ").append(ontClass.getURI());
        }
        sb.append(DocumentationMarkup.CONTENT_END);

        sb.append(DocumentationMarkup.SECTIONS_START);
        setClassInfo(ontClass, sb);
        setPredicateInfo(individual, "Values", sb);
        sb.append(DocumentationMarkup.SECTIONS_END);
        return sb.toString();
    }

    private static String getClassDocumentation(ODTResolvableQualifiedUriStep step) {
        String fullyQualifiedUri = step.getFullyQualifiedUri();
        OntClass ontClass = OntologyModel.getInstance().getClass(fullyQualifiedUri);
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
        Set<? extends OntResource> instances = OntologyModel.getInstance().toIndividuals(ontClass).stream()
                .filter(resource -> resource.getURI() != null && !resource.getURI().endsWith("_INSTANCE"))
                .collect(Collectors.toSet());
        if (!instances.isEmpty()) {
            sb.append(DocumentationProvider.getKeyValueSection("Instances:",
                    OntologyResourceUtil.describeUrisJoined(instances, "<br>", false)));
        }
        sb.append(DocumentationMarkup.SECTIONS_END);
        return sb.toString();
    }

    private static void setPredicateInfo(OntResource resource, String header, StringBuilder sb) {
        Set<Property> properties = getProperties(resource);

        String propertiesMap = properties.stream()
                .map(property -> getPropertyInfo(resource, property))
                .filter(Strings::isNotBlank)
                .collect(Collectors.joining());
        if (Strings.isNotBlank(propertiesMap)) {
            String propertiesTable = "<table>" + propertiesMap + "</table>";
            sb.append(DocumentationProvider.getKeyValueSection(header, propertiesTable));
        }
    }

    private static String getPropertyInfo(OntResource resource,
                                          Property property) {
        if (!property.equals(OntologyModelConstants.getRdfType())) {
            RDFNode propertyValue = resource.getPropertyValue(property);
            if (propertyValue == null && resource instanceof OntClass) {
                propertyValue = getFromSuperclass((OntClass) resource, property);
            }

            return Optional.ofNullable(propertyValue)
                    .map(ODTUriStepDocumentationUtil::getQualifiedPropertyName)
                    .map(propertyUri -> getPropertyDescription(property.getLocalName(), propertyUri))
                    .orElse(null);

        }
        return null;
    }

    private static String getPropertyDescription(String localname, String propertyUri) {
        return "<tr><td style=\"padding-right: 5px\">" +
                localname +
                "</td><td>" +
                extractValue(propertyUri)
                + "</td></tr>";
    }

    private static String getQualifiedPropertyName(RDFNode propertyValue) {
        if (propertyValue.isLiteral()) {
            return propertyValue.asLiteral().getValue().toString();
        } else if (propertyValue.isResource()) {
            return propertyValue.asResource().getLocalName();
        } else {
            return null;
        }
    }

    private static RDFNode getFromSuperclass(OntClass ontClass, Property property) {
        return OntologyModel.getInstance().listSuperClasses(ontClass)
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
        return OntologyModel.getInstance().listPredicates(OntologyModel.getInstance().toClass(resource))
                .stream()
                .filter(property -> !OntologyModelConstants.getClassModelProperties().contains(property))
                .collect(Collectors.toSet());
    }

    private static void setClassInfo(OntClass ontClass, StringBuilder sb) {
        // the order is important for super and subclasses, use list instead of set
        Set<OntClass> superClasses = OntologyModel.getInstance().listSuperClasses(ontClass);
        if (!superClasses.isEmpty()) {
            sb.append(DocumentationProvider.getKeyValueSection("Superclasses:",
                    superClasses.stream().map(uri -> OntologyResourceUtil.describeUri(uri, false))
                            .distinct()
                            .collect(Collectors.joining("<br>"))));
        }

        Set<OntClass> subClasses = OntologyModel.getInstance().listSubclasses(ontClass);
        if (!subClasses.isEmpty()) {
            sb.append(DocumentationProvider.getKeyValueSection("Subclasses:",
                    subClasses.stream().map(uri -> OntologyResourceUtil.describeUri(uri, false))
                            .distinct()
                            .collect(Collectors.joining("<br>"))));
        }

        setPredicateInfo(ontClass, "Predicates", sb);
    }

    private static String getCardinalityDetail(String cardinality) {
        if (cardinality == null) {
            return null;
        }
        if (cardinality.equals("*")) {
            return "0 or more";
        }
        if (cardinality.equals("?")) {
            return "0 or 1";
        }
        if (cardinality.equals("+")) {
            return "1 or more";
        }
        if (cardinality.equals("1")) {
            return "exactly one";
        }
        return "";
    }

    private static String getTraverseDocumentation(ODTResolvableQualifiedUriStep step) {
        PsiElement parent = step.getParent();
        boolean isReversed = parent instanceof ODTQueryReverseStep;
        Set<OntResource> unfiltered = isReversed ? ((ODTResolvable) parent).resolve() : step.resolve();
        Set<OntResource> filtered = step.getResolvableParent().filter(unfiltered);
        String fullyQualifiedUri = step.getFullyQualifiedUri();

        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append("Predicate: ").append(fullyQualifiedUri);
        addCardinality(parent, step, filtered, isReversed, sb);
        sb.append(DocumentationMarkup.DEFINITION_END);

        sb.append(DocumentationMarkup.CONTENT_START);
        sb.append(getContentMessage(isReversed));
        sb.append(DocumentationMarkup.CONTENT_END);

        sb.append(DocumentationMarkup.SECTIONS_START);
        sb.append(DocumentationProvider.getKeyValueSection("Direction:", isReversed ? "Reverse" : "Forward"));
        addNextStep(fullyQualifiedUri, isReversed, unfiltered, filtered, sb);
        addPreviousStep(step, isReversed, sb);

        sb.append(DocumentationMarkup.SECTIONS_END);
        return sb.toString();
    }

    private static void addNextStep(String fullyQualifiedUri,
                                    boolean isReversed,
                                    Set<OntResource> unfiltered,
                                    Set<OntResource> filtered,
                                    StringBuilder sb) {
        if (OntologyModel.getInstance().getProperty(fullyQualifiedUri) != null) {
            String label = isReversed ? "Subject(s)" : "Object(s)";
            sb.append(DocumentationProvider.getKeyValueSection("Result", "<u>" + label + "</u><br>" + OntologyResourceUtil.describeUrisJoined(filtered, "<br>", false)));
            if (unfiltered.size() != filtered.size()) {
                sb.append(DocumentationProvider.getKeyValueSection("Unfiltered:", OntologyResourceUtil.describeUrisJoined(unfiltered, "<br>", false)));
            }
        }
    }

    private static void addPreviousStep(ODTResolvableQualifiedUriStep step, boolean isReversed, StringBuilder sb) {
        Set<OntResource> previousStep = step.resolvePreviousStep();
        if (!previousStep.isEmpty()) {
            String label = isReversed ? "Object(s)" : "Subject(s)";
            sb.append(DocumentationProvider.getKeyValueSection("Previous step", "<u>" + label + "</u><br>" + OntologyResourceUtil.describeUrisJoined(previousStep, "<br>", false)));
        }
    }

    private static String getContentMessage(boolean reversed) {
        if (reversed) {
            return "When traversing the model <u>reversed</u>, the query will return anything in the TTL ontology which " +
                    "has the predicate (sh:path) and has the previous query step as object (sh:dataType / sh:class)";
        } else {
            return "When traversing the model, the query will return the object (sh:dataType / sh:class) of the " +
                    "previous query step (subject) using the predicate (sh:path)";
        }
    }

    private static void addCardinality(PsiElement parent,
                                       ODTResolvableQualifiedUriStep step,
                                       Set<OntResource> filtered,
                                       boolean isReversed,
                                       StringBuilder sb) {
        String cardinality = getCardinality(parent, step, filtered, isReversed);
        if (cardinality != null) {
            sb.append("<br>Cardinality");
            if (!cardinality.equals("1")) {
                sb.append(cardinality);
            }
            sb.append(": ");
            sb.append(getCardinalityDetail(cardinality)).append(" ");
        }
    }

    private static String getCardinality(PsiElement parent, ODTResolvableQualifiedUriStep step,
                                         Set<OntResource> filtered, boolean isReversed) {
        if (parent instanceof ODTQueryOperationStepImpl) {
            Set<OntResource> previous = ((ODTQueryOperationStepImpl) parent).resolvePreviousStep();
            return OntologyResourceUtil.getCardinalityLabel(previous, OntologyModel.getInstance().getProperty(step.getFullyQualifiedUri()));
        } else if (isReversed) {
            return OntologyResourceUtil.getCardinalityLabel(filtered, OntologyModel.getInstance().getProperty(step.getFullyQualifiedUri()));
        } else {
            return null;
        }
    }

    private static boolean isClassUri(ODTResolvableQualifiedUriStep step) {
        return OntologyModel.getInstance().getClass(step.getFullyQualifiedUri()) != null;
    }

    private static boolean isIndividualUri(ODTResolvableQualifiedUriStep step) {
        return OntologyModel.getInstance().getIndividual(step.getFullyQualifiedUri()) != null;
    }

}
