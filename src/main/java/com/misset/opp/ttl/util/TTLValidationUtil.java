package com.misset.opp.ttl.util;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.ResourceUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TTLValidationUtil {

    public static final String ERROR_MESSAGE_BOOLEAN = "Boolean type required";
    public static final String ERROR_MESSAGE_JSON = "Json object required";
    public static final String ERROR_MESSAGE_STRING = "String required";
    public static final String ERROR_MESSAGE_CLASSNAME = "Class required";
    public static final String ERROR_MESSAGE_DECIMAL = "Decimal required";
    public static final String ERROR_MESSAGE_INTEGER = "Integer required";
    public static final String ERROR_MESSAGE_NUMBER = "Number required";
    public static final String ERROR_MESSAGE_DATE_TIME = "Date(time) required";
    public static final String ERROR_MESSAGE_NAMED_GRAPH = "Namedgraph instance required";
    public static final String ERROR_MESSAGE_INSTANCES = "Instance of class required";
    public static final String ERROR_MESSAGE_GRAPH_SHAPE = "GraphShape required";

    public static void validateCompatibleTypes(Set<OntResource> resourcesA,
                                               Set<OntResource> resourcesB,
                                               ProblemsHolder holder,
                                               PsiElement element) {
        if (resourcesA == null || resourcesB == null || resourcesA.isEmpty() || resourcesB.isEmpty()) {
            // don't register problem
            return;
        }
        if (!OppModel.INSTANCE.areCompatible(resourcesA, resourcesB)) {
            holder.registerProblem(element,
                    createIncompatibleTypesWarning(resourcesA, resourcesB),
                    ProblemHighlightType.WARNING);
        }
    }

    public static void validateModularityMultiple(Set<OntResource> subject,
                                                  Property predicate,
                                                  ProblemsHolder holder,
                                                  PsiElement element) {
        if (OppModel.INSTANCE.isSingleton(subject, predicate)) {
            // assigning a collection or creating a collection where a singleton is expected
            holder.registerProblem(element,
                    "Suspicious assignment: " + predicate.getLocalName() + " maxCount == 1",
                    ProblemHighlightType.WARNING);
        }
    }

    public static void validateHasOntClass(Set<OntResource> resources,
                                           ProblemsHolder holder,
                                           PsiElement element,
                                           Set<OntClass> classes) {
        if (resources.isEmpty()) {
            return;
        }
        if (!resources.stream().allMatch(
                ontResource -> hasOntClass(ontResource, classes)
        )) {
            Set<OntResource> acceptableIndividuals = classes.stream().map(ontClass -> OppModel.INSTANCE.getClassIndividuals(ontClass.getURI()))
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .map(individual -> (OntResource) individual)
                    .collect(Collectors.toSet());
            String message = "Acceptable types: " + ResourceUtil.describeUrisJoined(acceptableIndividuals);
            holder.registerProblem(element, message, ProblemHighlightType.ERROR);
        }
    }
    private static boolean hasOntClass(OntResource resource,
                                       Set<OntClass> classes) {
        List<OntClass> ontClasses = resource.isIndividual() ?
                resource.asIndividual().listOntClasses(false).toList() : Collections.emptyList();
        return ontClasses.stream().anyMatch(classes::contains);
    }

    private static boolean validate(Set<OntResource> resources,
                                    ProblemsHolder holder,
                                    PsiElement element,
                                    Predicate<OntResource> condition,
                                    String error) {
        if (!resources.isEmpty() && !resources.stream().allMatch(condition)) {
            holder.registerProblem(element, error, ProblemHighlightType.ERROR);
            return false;
        }
        return true;
    }

    public static void validateNamedGraph(Set<OntResource> resources,
                                          ProblemsHolder holder,
                                          PsiElement element) {
        validate(resources, holder, element, TTLValidationUtil::isNamedGraphInstance, ERROR_MESSAGE_NAMED_GRAPH);
    }

    public static void validateInstances(Set<OntResource> resources,
                                         ProblemsHolder holder,
                                         PsiElement element) {
        validate(resources, holder, element, OntResource::isIndividual, ERROR_MESSAGE_INSTANCES);
    }

    public static boolean validateBoolean(Set<OntResource> resources,
                                          ProblemsHolder holder,
                                          PsiElement element) {
        return validate(resources,
                holder,
                element,
                OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE::equals,
                ERROR_MESSAGE_BOOLEAN);
    }

    public static void validateDecimal(Set<OntResource> resources,
                                       ProblemsHolder holder,
                                       PsiElement element) {
        validate(resources,
                holder,
                element,
                OppModel.INSTANCE.XSD_DECIMAL_INSTANCE::equals,
                ERROR_MESSAGE_DECIMAL);
    }


    public static void validateInteger(Set<OntResource> resources, ProblemsHolder holder, PsiElement element) {
        validate(resources,
                holder,
                element,
                OppModel.INSTANCE.XSD_INTEGER::equals,
                ERROR_MESSAGE_INTEGER);
    }

    public static void validateNumber(Set<OntResource> resources,
                                      ProblemsHolder holder,
                                      PsiElement element) {
        validate(resources,
                holder,
                element,
                ontResource ->
                        ontResource.isIndividual() &&
                                ontResource.asIndividual()
                                        .listOntClasses(false)
                                        .toList()
                                        .contains(OppModel.INSTANCE.XSD_NUMBER),
                ERROR_MESSAGE_NUMBER);
    }

    public static void validateJSON(Set<OntResource> resources,
                                    ProblemsHolder holder,
                                    PsiElement element) {
        validate(resources, holder, element, OppModel.INSTANCE.JSON_OBJECT::equals, ERROR_MESSAGE_JSON);
    }

    public static boolean validateString(Set<OntResource> resources,
                                         ProblemsHolder holder,
                                         PsiElement element) {
        return validate(resources,
                holder,
                element,
                OppModel.INSTANCE.XSD_STRING_INSTANCE::equals,
                ERROR_MESSAGE_STRING);
    }

    public static void validateClassName(Set<OntResource> resources,
                                         ProblemsHolder holder,
                                         PsiElement element) {
        validate(resources, holder, element, OntResource::isClass, ERROR_MESSAGE_CLASSNAME);
    }

    public static void validateGraphShape(Set<OntResource> resources,
                                          ProblemsHolder holder,
                                          PsiElement element) {
        validate(resources, holder, element, TTLValidationUtil::isGraphshapeInstance, ERROR_MESSAGE_GRAPH_SHAPE);
    }

    public static void validateDateTime(Set<OntResource> resources, ProblemsHolder holder, PsiElement element) {
        validate(resources, holder, element,
                ontResource ->
                        ontResource.isIndividual() &&
                                ontResource.asIndividual()
                                        .listOntClasses(false)
                                        .toList()
                                        .contains(OppModel.INSTANCE.XSD_DATETIME),
                ERROR_MESSAGE_DATE_TIME);
    }

    private static boolean isNamedGraphInstance(OntResource resource) {
        return resource.isIndividual() && resource.asIndividual()
                .listOntClasses(false)
                .toList()
                .contains(OppModel.INSTANCE.NAMED_GRAPH_CLASS);
    }

    private static boolean isGraphshapeInstance(OntResource resource) {
        return resource.isIndividual() && resource.asIndividual()
                .listOntClasses(false)
                .toList()
                .contains(OppModel.INSTANCE.GRAPH_SHAPE);
    }

    private static String createIncompatibleTypesWarning(Set<OntResource> resourcesA,
                                                         Set<OntResource> resourcesB) {
        return "Incompatible types:" + "\n" +
                "cannot assign " + "\n" +
                ResourceUtil.describeUrisJoined(resourcesB) + "\n" +
                "to" + "\n" +
                ResourceUtil.describeUrisJoined(resourcesA);
    }

}
