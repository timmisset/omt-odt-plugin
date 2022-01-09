package com.misset.opp.odt.psi.impl.resolvable.queryStep;

import com.intellij.lang.documentation.DocumentationMarkup;
import com.misset.opp.odt.documentation.ODTDocumentationProvider;
import com.misset.opp.odt.psi.ODTQueryReverseStep;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ODTResolvableQualifiedUriStepDocumentationUtil {

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
        Set<? extends OntResource> instances = ontClass.listInstances().toSet().stream()
                .filter(resource -> !resource.getURI().endsWith("_INSTANCE"))
                .collect(Collectors.toSet());
        if (!instances.isEmpty()) {
            ODTDocumentationProvider.addKeyValueSection("Instances:",
                    TTLResourceUtil.describeUrisJoined(instances, "<br>", false)
                    , sb);
        }
        sb.append(DocumentationMarkup.SECTIONS_END);
        return sb.toString();
    }

    private static void setClassInfo(OntClass ontClass, StringBuilder sb) {
        // the order is important for super and subclasses, use list instead of set
        List<OntClass> superClasses = OppModel.INSTANCE.listOrderedSuperClasses(ontClass);
        if (!superClasses.isEmpty()) {
            ODTDocumentationProvider.addKeyValueSection("Superclasses:",
                    superClasses.stream().map(uri -> TTLResourceUtil.describeUri(uri, false))
                            .distinct()
                            .collect(Collectors.joining("<br>"))
                    , sb);
        }


        List<OntClass> subClasses = OppModel.INSTANCE.listOrderedSubClasses(ontClass);
        if (!subClasses.isEmpty()) {
            ODTDocumentationProvider.addKeyValueSection("Subclasses:",
                    subClasses.stream().map(uri -> TTLResourceUtil.describeUri(uri, false))
                            .distinct()
                            .collect(Collectors.joining("<br>"))
                    , sb);
        }

        Set<OntProperty> properties = OppModel.INSTANCE.listPredicates(ontClass)
                .stream()
                .filter(OntProperty.class::isInstance)
                .map(OntProperty.class::cast)
                .collect(Collectors.toSet());
        if (!properties.isEmpty()) {
            ODTDocumentationProvider.addKeyValueSection("Predicates:",
                    TTLResourceUtil.describeUrisJoined(properties, "<br>", false)
                    , sb);
        }

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

        ODTDocumentationProvider.addKeyValueSection("Direction:", isReversed ? "Reverse" : "Forward", sb);

        if (OppModel.INSTANCE.getProperty(fullyQualifiedUri) != null) {
            String label = isReversed ? "Subject(s)" : "Object(s)";
            ODTDocumentationProvider.addKeyValueSection("Result", "<u>" + label + "</u><br>" + TTLResourceUtil.describeUrisJoined(filtered, "<br>", false), sb);
            if (unfiltered.size() != filtered.size()) {
                ODTDocumentationProvider.addKeyValueSection("Unfiltered:", TTLResourceUtil.describeUrisJoined(unfiltered, "<br>", false), sb);
            }
        }

        Set<OntResource> previousStep = step.resolvePreviousStep();
        if (!previousStep.isEmpty()) {
            String label = isReversed ? "Object(s)" : "Subject(s)";
            ODTDocumentationProvider.addKeyValueSection("Previous step", "<u>" + label + "</u><br>" + TTLResourceUtil.describeUrisJoined(previousStep, "<br>", false), sb);
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
