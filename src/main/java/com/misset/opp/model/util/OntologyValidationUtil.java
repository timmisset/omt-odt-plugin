package com.misset.opp.model.util;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.Strings;
import com.intellij.psi.PsiElement;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.OntologyModelTranslator;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public final class OntologyValidationUtil {

    private final OntologyModel ontologyModel;
    private final OntologyResourceUtil resourceUtil;

    public OntologyValidationUtil(Project project) {
        ontologyModel = OntologyModel.getInstance(project);
        resourceUtil = OntologyResourceUtil.getInstance(project);
    }

    public static OntologyValidationUtil getInstance(Project project) {
        return project.getService(OntologyValidationUtil.class);
    }

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

    public void validateCompatibleTypes(Set<OntResource> resourcesA,
                                        Set<OntResource> resourcesB,
                                        ProblemsHolder holder,
                                        PsiElement element) {
        if (resourcesA == null ||
                resourcesB == null ||
                resourcesA.isEmpty() ||
                resourcesB.isEmpty() ||
                resourcesA.contains(OntologyModelConstants.getOwlThingInstance()) ||
                resourcesB.contains(OntologyModelConstants.getOwlThingInstance())
        ) {
            // don't register problem
            return;
        }
        if (!ontologyModel.areCompatible(resourcesA, resourcesB)) {
            holder.registerProblem(element,
                    createIncompatibleTypesWarning(resourcesA, resourcesB),
                    ProblemHighlightType.WARNING);
        }
    }

    public void validateRequiredTypes(Set<OntResource> required,
                                      Set<OntResource> provided,
                                      ProblemsHolder holder,
                                      PsiElement element) {
        if (required == null || provided == null || required.isEmpty() || provided.isEmpty()) {
            // don't register problem
            return;
        }
        if (!ontologyModel.areCompatible(required, provided)) {
            holder.registerProblem(element,
                    createRequiredTypesWarning(required, provided),
                    ProblemHighlightType.WARNING);
        }
    }

    public void validateCardinalityMultiple(Set<OntResource> subject,
                                            Property predicate,
                                            ProblemsHolder holder,
                                            PsiElement element) {
        if (OntologyModelTranslator.getInstance(holder.getProject()).isSingleton(subject, predicate)) {
            // assigning a collection or creating a collection where a singleton is expected
            holder.registerProblem(element,
                    "Suspicious assignment: " + predicate.getLocalName() + " maxCount == 1",
                    ProblemHighlightType.WARNING);
        }
    }

    public void validateHasOntClass(Set<OntResource> resources,
                                    ProblemsHolder holder,
                                    PsiElement element,
                                    Set<OntClass> classes) {
        if (resources.isEmpty()) {
            return;
        }
        if (!resources.stream().allMatch(
                ontResource -> hasOntClass(ontResource, classes)
        )) {
            Set<OntResource> acceptableIndividuals =
                    classes.stream().map(ontClass -> ontologyModel.toIndividuals(ontClass.getURI()))
                            .flatMap(Collection::stream)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toSet());
            String message = "Acceptable types: " + OntologyResourceUtil.getInstance(holder.getProject()).describeUrisJoined(acceptableIndividuals);
            holder.registerProblem(element, message, ProblemHighlightType.ERROR);
        }
    }

    private boolean hasOntClass(OntResource resource,
                                Set<OntClass> classes) {
        return ontologyModel.isIndividual(resource) &&
                ontologyModel.listOntClasses(resource).stream().anyMatch(classes::contains);
    }

    private boolean validate(Set<OntResource> resources,
                             ProblemsHolder holder,
                             PsiElement element,
                             Predicate<OntResource> condition,
                             String error) {
        if (!resources.isEmpty() &&
                !resources.contains(OntologyModelConstants.getOwlThingInstance()) &&
                !resources.stream().allMatch(condition)) {
            holder.registerProblem(element, error, ProblemHighlightType.ERROR);
            return false;
        }
        return true;
    }

    public void validateNamedGraph(Set<OntResource> resources,
                                   ProblemsHolder holder,
                                   PsiElement element) {
        validate(resources, holder, element, this::isNamedGraphInstance, ERROR_MESSAGE_NAMED_GRAPH);
    }

    public void validateInstances(Set<OntResource> resources,
                                  ProblemsHolder holder,
                                  PsiElement element) {
        validate(resources, holder, element, ontologyModel::isIndividual, ERROR_MESSAGE_INSTANCES);
    }

    public boolean validateBoolean(Set<OntResource> resources,
                                   ProblemsHolder holder,
                                   PsiElement element) {
        return validate(resources,
                holder,
                element,
                resource -> ontologyModel.isInstanceOf(resource, OntologyModelConstants.getXsdBoolean()),
                ERROR_MESSAGE_BOOLEAN);
    }

    public void validateDecimal(Set<OntResource> resources,
                                ProblemsHolder holder,
                                PsiElement element) {
        validate(resources,
                holder,
                element,
                OntologyModelConstants.getXsdDecimalInstance()::equals,
                ERROR_MESSAGE_DECIMAL);
    }

    public void validateInteger(Set<OntResource> resources, ProblemsHolder holder, PsiElement element) {
        validate(resources,
                holder,
                element,
                OntologyModelConstants.getXsdIntegerInstance()::equals,
                ERROR_MESSAGE_INTEGER);
    }

    public void validateNumber(Set<OntResource> resources,
                               ProblemsHolder holder,
                               PsiElement element) {
        validate(resources, holder, element,
                resource -> isIndividualOfType(resource, OntologyModelConstants.getXsdNumber()),
                ERROR_MESSAGE_NUMBER);
    }

    public void validateJSON(Set<OntResource> resources,
                             ProblemsHolder holder,
                             PsiElement element) {
        validate(resources, holder, element, OntologyModelConstants.getJsonObject()::equals, ERROR_MESSAGE_JSON);
    }

    public boolean validateString(Set<OntResource> resources,
                                  ProblemsHolder holder,
                                  PsiElement element) {
        return validate(resources,
                holder,
                element,
                OntologyModelConstants.getXsdStringInstance()::equals,
                ERROR_MESSAGE_STRING);
    }

    public void validateClassName(Set<OntResource> resources,
                                  ProblemsHolder holder,
                                  PsiElement element) {
        validate(resources, holder, element, OntologyModel.getInstance(holder.getProject())::isClass, ERROR_MESSAGE_CLASSNAME);
    }

    public void validateGraphShape(Set<OntResource> resources,
                                   ProblemsHolder holder,
                                   PsiElement element) {
        validate(resources, holder, element, this::isGraphshapeInstance, ERROR_MESSAGE_GRAPH_SHAPE);
    }

    public void validateDateTime(Set<OntResource> resources, ProblemsHolder holder, PsiElement element) {
        validate(resources, holder, element,
                resource ->
                        isIndividualOfType(resource, OntologyModelConstants.getXsdDatetime()),
                ERROR_MESSAGE_DATE_TIME);
    }

    private boolean isNamedGraphInstance(OntResource resource) {
        return isIndividualOfType(resource, OntologyModelConstants.getNamedGraphClass());
    }

    public boolean isGraphshapeInstance(OntResource resource) {
        return isIndividualOfType(resource, OntologyModelConstants.getGraphShape());
    }

    private boolean isIndividualOfType(OntResource resource, OntClass type) {
        return ontologyModel.isIndividual(resource) &&
                ontologyModel.listOntClasses(resource).contains(type);
    }

    private String createIncompatibleTypesWarning(Set<OntResource> resourcesA,
                                                  Set<OntResource> resourcesB) {
        return "Incompatible types:" + "\n" +
                "cannot assign " + "\n" +
                resourceUtil.describeUrisJoined(resourcesB) + "\n" +
                "to" + "\n" +
                resourceUtil.describeUrisJoined(resourcesA);
    }

    private String createRequiredTypesWarning(Set<OntResource> required,
                                              Set<OntResource> provided) {
        return "Incompatible types:" + "\n" +
                "required " + "\n" +
                resourceUtil.describeUrisJoined(required) + "\n" +
                "provided" + "\n" +
                resourceUtil.describeUrisJoined(provided);
    }

    public void validateValues(Set<String> paramValues,
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
