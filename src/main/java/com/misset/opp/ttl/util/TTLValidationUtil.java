package com.misset.opp.ttl.util;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.text.Strings;
import com.intellij.psi.PsiElement;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.model.OppModelTranslator;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
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
    public static final String ERROR_MESSAGE_NAMED_GRAPH = "NamedGraph instance required";
    public static final String ERROR_MESSAGE_INSTANCES = "Instance of class required";
    public static final String ERROR_MESSAGE_GRAPH_SHAPE = "GraphShape required";

    public static void validateCompatibleTypes(Set<OntResource> resourcesA,
                                               Set<OntResource> resourcesB,
                                               ProblemsHolder holder,
                                               PsiElement element) {
        if (resourcesA == null ||
                resourcesB == null ||
                resourcesA.isEmpty() ||
                resourcesB.isEmpty() ||
                resourcesA.contains(OppModelConstants.OWL_THING_INSTANCE) ||
                resourcesB.contains(OppModelConstants.OWL_THING_INSTANCE)
        ) {
            // don't register problem
            return;
        }
        if (!OppModel.INSTANCE.areCompatible(resourcesA, resourcesB)) {
            holder.registerProblem(element,
                    createIncompatibleTypesWarning(resourcesA, resourcesB),
                    ProblemHighlightType.WARNING);
        }
    }

    public static void validateRequiredTypes(Set<OntResource> required,
                                             Set<OntResource> provided,
                                             ProblemsHolder holder,
                                             PsiElement element) {
        if (required == null || provided == null || required.isEmpty() || provided.isEmpty()) {
            // don't register problem
            return;
        }
        if (!OppModel.INSTANCE.areCompatible(required, provided)) {
            holder.registerProblem(element,
                    createRequiredTypesWarning(required, provided),
                    ProblemHighlightType.WARNING);
        }
    }

    public static void validateCardinalityMultiple(Set<OntResource> subject,
                                                   Property predicate,
                                                   ProblemsHolder holder,
                                                   PsiElement element) {
        if (OppModelTranslator.isSingleton(subject, predicate)) {
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
            Set<OntResource> acceptableIndividuals = classes.stream().map(ontClass -> OppModel.INSTANCE.toIndividuals(ontClass.getURI()))
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            String message = "Acceptable types: " + TTLResourceUtil.describeUrisJoined(acceptableIndividuals);
            holder.registerProblem(element, message, ProblemHighlightType.ERROR);
        }
    }

    private static boolean hasOntClass(OntResource resource,
                                       Set<OntClass> classes) {
        return OppModel.INSTANCE.isIndividual(resource) &&
                OppModel.INSTANCE.listOntClasses(resource).stream().anyMatch(classes::contains);
    }

    private static boolean validate(Set<OntResource> resources,
                                    ProblemsHolder holder,
                                    PsiElement element,
                                    Predicate<OntResource> condition,
                                    String error) {
        if (!resources.isEmpty() &&
                !resources.contains(OppModelConstants.OWL_THING_INSTANCE) &&
                !resources.stream().allMatch(condition)) {
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
        validate(resources, holder, element, OppModel.INSTANCE::isIndividual, ERROR_MESSAGE_INSTANCES);
    }

    public static boolean validateBoolean(Set<OntResource> resources,
                                          ProblemsHolder holder,
                                          PsiElement element) {
        return validate(resources,
                holder,
                element,
                OppModelConstants.XSD_BOOLEAN_INSTANCE::equals,
                ERROR_MESSAGE_BOOLEAN);
    }

    public static void validateDecimal(Set<OntResource> resources,
                                       ProblemsHolder holder,
                                       PsiElement element) {
        validate(resources,
                holder,
                element,
                OppModelConstants.XSD_DECIMAL_INSTANCE::equals,
                ERROR_MESSAGE_DECIMAL);
    }

    public static void validateInteger(Set<OntResource> resources, ProblemsHolder holder, PsiElement element) {
        validate(resources,
                holder,
                element,
                OppModelConstants.XSD_INTEGER_INSTANCE::equals,
                ERROR_MESSAGE_INTEGER);
    }

    public static void validateNumber(Set<OntResource> resources,
                                      ProblemsHolder holder,
                                      PsiElement element) {
        validate(resources, holder, element,
                resource -> isIndividualOfType(resource, OppModelConstants.XSD_NUMBER),
                ERROR_MESSAGE_NUMBER);
    }

    public static void validateJSON(Set<OntResource> resources,
                                    ProblemsHolder holder,
                                    PsiElement element) {
        validate(resources, holder, element, OppModelConstants.JSON_OBJECT::equals, ERROR_MESSAGE_JSON);
    }

    public static boolean validateString(Set<OntResource> resources,
                                         ProblemsHolder holder,
                                         PsiElement element) {
        return validate(resources,
                holder,
                element,
                OppModelConstants.XSD_STRING_INSTANCE::equals,
                ERROR_MESSAGE_STRING);
    }

    public static void validateClassName(Set<OntResource> resources,
                                         ProblemsHolder holder,
                                         PsiElement element) {
        validate(resources, holder, element, OppModel.INSTANCE::isClass, ERROR_MESSAGE_CLASSNAME);
    }

    public static void validateGraphShape(Set<OntResource> resources,
                                          ProblemsHolder holder,
                                          PsiElement element) {
        validate(resources, holder, element, TTLValidationUtil::isGraphshapeInstance, ERROR_MESSAGE_GRAPH_SHAPE);
    }

    public static void validateDateTime(Set<OntResource> resources, ProblemsHolder holder, PsiElement element) {
        validate(resources, holder, element,
                resource ->
                        isIndividualOfType(resource, OppModelConstants.XSD_DATETIME),
                ERROR_MESSAGE_DATE_TIME);
    }

    private static boolean isNamedGraphInstance(OntResource resource) {
        return isIndividualOfType(resource, OppModelConstants.NAMED_GRAPH_CLASS);
    }

    public static boolean isGraphshapeInstance(OntResource resource) {
        return isIndividualOfType(resource, OppModelConstants.GRAPH_SHAPE);
    }

    private static boolean isIndividualOfType(OntResource resource, OntClass type) {
        return OppModel.INSTANCE.isIndividual(resource) &&
                OppModel.INSTANCE.listOntClasses(resource).contains(type);
    }

    private static String createIncompatibleTypesWarning(Set<OntResource> resourcesA,
                                                         Set<OntResource> resourcesB) {
        return "Incompatible types:" + "\n" +
                "cannot assign " + "\n" +
                TTLResourceUtil.describeUrisJoined(resourcesB) + "\n" +
                "to" + "\n" +
                TTLResourceUtil.describeUrisJoined(resourcesA);
    }

    private static String createRequiredTypesWarning(Set<OntResource> required,
                                                     Set<OntResource> provided) {
        return "Incompatible types:" + "\n" +
                "required " + "\n" +
                TTLResourceUtil.describeUrisJoined(required) + "\n" +
                "provided" + "\n" +
                TTLResourceUtil.describeUrisJoined(provided);
    }

    public static void validateValues(Set<String> paramValues,
                                      String argumentValue,
                                      ProblemsHolder holder,
                                      PsiElement signatureArgumentElement) {
        if (paramValues.isEmpty() || argumentValue.isBlank() || paramValues.contains(argumentValue)) {
            return;
        }

        holder.registerProblem(
                signatureArgumentElement,
                "Incompatible value, expected: " + Strings.join(paramValues, ", "),
                ProblemHighlightType.ERROR);
    }
}
