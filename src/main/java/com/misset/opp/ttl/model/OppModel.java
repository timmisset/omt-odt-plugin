package com.misset.opp.ttl.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SimpleModificationTracker;
import com.misset.opp.omt.OMTFileType;
import com.misset.opp.settings.SettingsState;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Wrapper for OntModel (Apache Jena) which has methods to quickly resolve the SHACL implementation of the OPP ontology
 * The wrapper works with 2 implementations of the Ontology Resource: OntClass and Individual
 * <p>
 * When a query resolves a step using the rdf:type, the outcome will be OntClass
 * When a query resolves a step using the ^rdf:type, the outcome will be Individual
 * <p>
 * <p>
 * Note:
 * While performance testing the plugin, it became apparent that the Apache Jena model is not really optimized for performance.
 * Probably with good reason but there seems to be no basic caching mechanism which is why querying the model with the same input
 * multiple times will always re-run the entire query.
 * Since we have a good handle on when the model is updated we can safely assume that, when there is no change in the TTL model,
 * using the same query multiple times will always return the same result and thus should be cached.
 */
public class OppModel {
    private static final OppModelCache modelCache = new OppModelCache();

    private static final Logger LOGGER = Logger.getInstance(OppModel.class);
    /*
        The modification count whenever the model is loaded
        Any RDF resolving cached values should subscribe to this modification tracker to drop their
        results when something is changed in the model
     */
    public static SimpleModificationTracker ONTOLOGY_MODEL_MODIFICATION_TRACKER = new SimpleModificationTracker();

    // create a default empty model to begin with until the DumbService is smart (finished indexing)
    // and the ontology can be loaded using the FileIndex
    public static OppModel INSTANCE = new OppModel(ModelFactory.createOntologyModel());

    HashMap<String, OntResource> mappedResourcesCache = new HashMap<>();

    /**
     * The simple model contains a simple representation of the SHACL based model that is initially loaded
     */
    private OntModel model;

    public OppModel(OntModel shaclModel) {
        OntModel ontologyModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RDFS_INF);

        LoggerUtil.runWithLogger(LOGGER, "Loading OppModel from Shacl", () -> {
            // the OWL_DL restricts types, a resource can only be either a class, a property or an instance, not multiple at the same time
            // the RDFS_INF inferencing provides the support for sub/superclass logic
            incrementModificationCount();
            OppModelConstants.setConstants(this, modelCache, ontologyModel);
            OppModelTranslator.loadSimpleModel(this, modelCache, ontologyModel, shaclModel);
            modelCache.cacheClassesTree();
            INSTANCE = this;
            this.model = ontologyModel;
        });
    }

    /**
     * Every time a new instance of OppModel is created, the entire model cache is flushed and the model is reloaded
     * The modification tracker is used by the CacheValuesManager of IntelliJ to determine if resolved values on PsiElements
     * need to be recalculated.
     */
    private void incrementModificationCount() {
        ONTOLOGY_MODEL_MODIFICATION_TRACKER.incModificationCount();
        mappedResourcesCache.clear();
        modelCache.flush();
    }

    protected OntModel getShaclModel() {
        return OppModelTranslator.getShaclModel();
    }

    public OntModel getModel() {
        return model;
    }

    public Set<OntClass> listClasses() {
        return modelCache.getClasses();
    }

    /**
     * List the subjects that have the specified predicate-object combination
     */
    public Set<OntResource> listSubjects(Property predicate,
                                         Set<OntResource> objects) {
        if (objects.contains(OppModelConstants.OWL_THING_INSTANCE)) {
            return Set.of(OppModelConstants.OWL_THING_INSTANCE);
        }
        return objects.stream()
                .map(classObject -> listSubjects(predicate, classObject))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * List the subjects that have the specified predicate-object combination
     */
    public Set<? extends OntResource> listSubjects(Property predicate,
                                                   OntResource object) {

        if (isIndividual(object) && OppModelConstants.RDF_TYPE.equals(predicate)) {
            // /ont:instanceOfClass / rdf:type => return ontology class of individual
            return Optional.ofNullable(toClass(object))
                    .map(Set::of)
                    .orElse(Collections.emptySet());
        } else if (isClass(object)) {
            if (OppModelConstants.RDF_TYPE.equals(predicate)) {
                // /ont:Class / ^rdf:type => return all individuals of the class
                return toIndividuals(object);
            } else if (OppModelConstants.RDFS_SUBCLASS_OF.equals(predicate)) {
                // return the subclasses of the provided object
                // /ont:ClassA / ^rdfs:subclassOf => /ont:ClassB (given B is a subclass of A)
                return modelCache.listSubclasses(getResourceId(object));
            }
        }
        return toType(modelCache.listSubjects(predicate, object), object);
    }

    /**
     * Filter the set of subjects to only ones that contain any of provided predicates
     */
    public Set<OntResource> filterSubjects(Set<OntResource> subjects,
                                           Set<OntResource> predicates) {
        return subjects.stream()
                .filter(subject -> hasAnyPredicate(subject, predicates))
                .collect(Collectors.toSet());
    }

    private boolean hasAnyPredicate(OntResource subject,
                                    Set<OntResource> predicates) {
        return predicates.stream()
                .map(Resource::getURI)
                .map(getModel()::getOntProperty)
                .anyMatch(predicate -> listPredicates(subject).contains(predicate));
    }

    /**
     * Filter the set of subjects to only ones that contain at least one of the predicates and one of the objects
     */
    public Set<OntResource> filterSubjects(Set<OntResource> subjects,
                                           Set<OntResource> predicates,
                                           Set<OntResource> objects) {
        return subjects.stream()
                .filter(subject -> hasAnyPredicateObject(subject, predicates, objects))
                .collect(Collectors.toSet());
    }

    private boolean hasAnyPredicateObject(OntResource subject,
                                          Set<OntResource> predicates,
                                          Set<OntResource> objects) {
        return predicates.stream()
                .map(Resource::getURI)
                .map(getModel()::getOntProperty)
                .anyMatch(predicate -> hasPredicateObjects(subject, predicate, objects));
    }

    private boolean hasPredicateObjects(OntResource subject,
                                        Property predicate,
                                        Set<OntResource> objects) {
        HashSet<OntResource> _objects = new HashSet<>(listObjects(subject, predicate));
        _objects.retainAll(objects);
        return !_objects.isEmpty();
    }

    private String getResourceId(OntResource resource) {
        return Optional.ofNullable(resource.getURI()).orElseGet(() -> resource.getId().toString());
    }

    /**
     * Converts the OntResource into an OntClass. If the provided resource is an individual, it will
     * return the ontClass of that individual
     */
    public @Nullable OntClass toClass(OntResource resource) {
        return modelCache.toClass(getResourceId(resource));
    }

    /**
     * Converts the OntResources into OntClasses. If the provided resource is an individual, it will
     * return the ontClass of that individual
     */
    public Set<OntClass> toClasses(Set<? extends OntResource> resources) {
        return resources.stream().map(this::toClass).collect(Collectors.toSet());
    }

    /**
     * Returns the super classes of the provided ontClass, not including itself
     */
    public Set<OntClass> getSuperClasses(OntClass ontClass) {
        return modelCache.getSuperclasses(getResourceId(ontClass), false);
    }

    /**
     * Returns the objects that the provided subjects link on the provided predicate
     * Different subjects can have different types using the same predicate. All types will be returned
     */
    public Set<OntResource> listObjects(Set<? extends OntResource> classSubjects,
                                        Property predicate) {
        if (classSubjects.contains(OppModelConstants.OWL_THING_INSTANCE)) {
            return Set.of(OppModelConstants.OWL_THING_INSTANCE);
        }
        return classSubjects.stream()
                .map(subject -> listObjects(subject, predicate))
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<? extends OntResource> listObjects(OntResource subject,
                                                   Property predicate) {
        if (isClass(subject) && OppModelConstants.RDF_TYPE.equals(predicate)) {
            if (OppModelConstants.RDF_TYPE.equals(predicate)) {
                // /ont:Class / rdf:type => return OWL_CLASS
                return Set.of(OppModelConstants.OWL_CLASS);
            } else if (OppModelConstants.RDFS_SUBCLASS_OF.equals(predicate)) {
                return modelCache.listSuperclasses(getResourceId(subject));
            }
        } else if (isIndividual(subject) && OppModelConstants.RDF_TYPE.equals(predicate)) {
            return Optional.ofNullable(toClass(subject)).map(Set::of).orElse(Collections.emptySet());
        }
        return toType(modelCache.listObjects(predicate, subject), subject);
    }

    /**
     * Returns all possible subclasses for the provided instances
     */
    public Set<OntResource> appendInstancesWithSubclasses(Set<OntResource> resources) {
        if (resources.stream().noneMatch(OppModelConstants.OWL_THING_INSTANCE::equals) &&
                resources.stream().allMatch(this::isIndividual)) {
            HashSet<OntResource> subclasses = resources.stream().map(this::toClass)
                    .map(this::listSubclasses)
                    .flatMap(Collection::stream)
                    .map(this::toIndividuals)
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(HashSet::new));
            subclasses.addAll(resources);
            return subclasses;
        } else {
            return resources;
        }
    }

    /**
     * Returns the ontology classes that this resource inherits / implements.
     * For example, ResourceB which is a subclass of ResourceA.
     * <p>
     * If resource is an instance of ResourceA it will return ResourceA
     * If resource is an instance of ResourceB it will return ResourceA and ResourceB
     * If resource is the class ResourceB it will return ResourceA and ResourceB
     */
    public Set<OntClass> listOntClasses(OntResource resource) {
        return Optional.ofNullable(toClass(resource))
                .map(ontClass -> {
                    final HashSet<OntClass> classes = new HashSet<>();
                    classes.add(ontClass);
                    classes.addAll(listSuperClasses(ontClass));
                    return classes;
                })
                .orElseGet(HashSet::new);
    }

    /**
     * Converts the provided resource into Individuals
     * If the resource is ClassA, it will return all instances of ClassA
     * If the resource is an instance of Class, it will return itself wrapped in a Set
     */
    public Set<Individual> toIndividuals(Set<? extends OntResource> resources) {
        return resources.stream()
                .map(this::toIndividuals)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    public Set<Individual> toIndividuals(OntResource resource) {
        return toIndividuals(getResourceId(resource));
    }

    public Set<Individual> toIndividuals(String resource) {
        return modelCache.toIndividuals(resource);
    }

    /**
     * List the predicates available on the provided resources
     */
    public Set<Property> listPredicates(Set<OntResource> classSubjects) {
        return classSubjects.stream()
                .map(this::listPredicates)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * List the predicates available on the provided resources
     */
    public Set<Property> listPredicates(OntResource ontResource) {
        Set<Property> properties = new HashSet<>(
                modelCache.listSubjectPredicates(toClass(ontResource)));
        if (isClass(ontResource)) {
            properties.addAll(Set.of(OppModelConstants.RDF_TYPE, OppModelConstants.RDFS_SUBCLASS_OF));
        } else if (isIndividual(ontResource)) {
            properties.add(OppModelConstants.RDF_TYPE);
        }
        return properties;
    }

    /**
     * List the predicates that point to the provided classes
     */
    public Set<Property> listReversePredicates(Set<OntResource> classSubjects) {
        return classSubjects.stream()
                .map(this::listReversePredicates)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * List the predicates that point to the provided resource
     */
    public Set<Property> listReversePredicates(OntResource ontResource) {
        Set<Property> properties = new HashSet<>(
                modelCache.listObjectPredicates(toClass(ontResource)));
        if (isClass(ontResource)) {
            properties.addAll(Set.of(OppModelConstants.RDF_TYPE, OppModelConstants.RDFS_SUBCLASS_OF));
        }
        return properties;
    }

    @Nullable
    public Individual getIndividual(@Nullable String uri) {
        return modelCache.getIndividual(uri);
    }

    public OntClass getClass(Resource resource) {
        return getClass(resource.getURI());
    }

    /**
     * Returns the OntClass only if it exists in the model
     * To translate an individual to its corresponding class, use toClass
     */
    @Nullable
    public OntClass getClass(@Nullable String uri) {
        return modelCache.getClass(uri);
    }

    public Boolean isClass(OntResource resource) {
        return modelCache.isClass(getResourceId(resource));
    }

    public Boolean isIndividual(OntResource resource) {
        return modelCache.isIndividual(getResourceId(resource));
    }

    public Boolean isPredicate(OntResource resource) {
        return modelCache.isProperty(getResourceId(resource));
    }

    public Property getProperty(Resource resource) {
        return modelCache.getProperty(resource);
    }

    @Nullable
    public Property getProperty(@Nullable String uri) {
        return modelCache.getProperty(uri);
    }

    /**
     * If only a single resource contains the given property, it will return that resource
     * if zero or more than 1 contain property, it will return null
     */
    public OntResource getUnambigiousResource(Property property) {
        return modelCache.getUnambigiousResource(property);
    }

    /**
     * Method to retrieve an existing Resource from the Ontology
     * If the resource doesn't exist but it can be matched to a known Iri (by RegEx pattern), it will be added
     * as an instance of the specified class.
     * For example, used to register known data graphs
     */
    @Nullable
    public OntResource getOntResource(String uri,
                                      Project project) {
        if (mappedResourcesCache.containsKey(uri)) {
            return mappedResourcesCache.get(uri);
        } else {
            final OntResource resource = getModel().getOntResource(uri);
            if (resource != null) {
                mappedResourcesCache.put(uri, resource);
                return resource;
            }

            if (project == null) {
                return null;
            }
            final Map<String, String> modelInstanceMapping = SettingsState.getInstance(project).modelInstanceMapping;
            return modelInstanceMapping.keySet()
                    .stream()
                    .filter(regEx -> Pattern.compile(regEx).matcher(uri).matches())
                    .map(modelInstanceMapping::get)
                    .map(getModel()::getOntClass)
                    .filter(Objects::nonNull)
                    .map(ontClass -> addIndividual(ontClass, uri, project))
                    .findFirst()
                    .orElse(null);
        }
    }

    public void createStatement(OntClass ontClass, Property property, Resource node) {
        ontClass.addProperty(property, node);
        modelCache.cache(ontClass, property, node);
    }

    public OntProperty createProperty(String uri) {
        return createProperty(uri, getModel());
    }

    OntProperty createProperty(String uri, OntModel model) {
        OntProperty property = model.createOntProperty(uri);
        modelCache.cache(property);
        return property;
    }

    public OntClass createClass(String uri) {
        return createClass(uri, getModel());
    }

    OntClass createClass(String uri, OntModel model) {
        OntClass ontClass = model.createClass(uri);
        modelCache.cache(ontClass);
        return ontClass;
    }

    private Individual addIndividual(OntClass ontClass, String uri, Project project) {
        if (mappedResourcesCache.containsKey(uri)) {
            // It's possible that multiple read threads come to the same conclusion
            // to add this Individual as write thread.
            return (Individual) mappedResourcesCache.get(uri);
        }
        final Individual individual = createIndividual(ontClass, uri);
        NotificationGroupManager.getInstance().getNotificationGroup("Update Ontology")
                .createNotification(
                        "Added " + getResourceId(individual) + " as " + OppModel.INSTANCE.toClass(individual),
                        NotificationType.INFORMATION)
                .setIcon(OMTFileType.INSTANCE.getIcon())
                .notify(project);
        mappedResourcesCache.put(uri, individual);

        SettingsState.getInstance(project).knownInstances.put(
                uri, getResourceId(ontClass)
        );
        return individual;
    }

    public Individual createIndividual(OntClass ontClass) {
        return createIndividual(ontClass, null);
    }

    public Individual createIndividual(OntClass ontClass, @Nullable String uri) {
        String classResourceId = getResourceId(ontClass);
        ontClass = modelCache.isClass(classResourceId) ?
                modelCache.getClass(classResourceId) :
                ontClass;

        final Individual individual = uri == null ? ontClass.createIndividual() : ontClass.createIndividual(uri);
        modelCache.cache(individual);
        return individual;
    }

    /**
     * Validate that 2 resources are compatible by the criteria that
     * resourceB is compatible to resourceA when resourceB is resourceA or any of the subclasses of resourceA.
     */
    public boolean areCompatible(Set<OntResource> resourcesA,
                                 Set<OntResource> resourcesB) {
        return LoggerUtil.computeWithLogger(LOGGER, "Comparing compatibility between " +
                        resourcesA + " and " + resourcesB,
                () -> resourcesB.stream()
                        .anyMatch(resourceB -> areCompatible(resourcesA, resourceB)));

    }

    private boolean areCompatible(Set<OntResource> resourcesA,
                                  OntResource resourceB) {
        return resourcesA.stream()
                .anyMatch(resourceA -> areCompatible(resourceA, resourceB));
    }

    private boolean areCompatible(OntResource resourceA,
                                  OntResource resourceB) {
        if (resourceA.equals(resourceB) ||
                resourceA.equals(OppModelConstants.OWL_THING_INSTANCE) ||
                resourceB.equals(OppModelConstants.OWL_THING_INSTANCE)) {
            return true;
        }
        if (isIndividual(resourceA)) {
            // compare individuals by their classes
            if (!isIndividual(resourceB)) {
                return false;
            } else {
                // for 2 individuals, check if classA is part of the classes for B
                return listOntClasses(resourceB)
                        .contains(toClass(resourceA));
            }
        }
        if (isPredicate(resourceA) || isPredicate(resourceB)) {
            // properties are only compatible when equal
            return resourceA.equals(resourceB);
        }
        if (isClass(resourceA)) {
            if (!isClass(resourceB)) {
                return false;
            } else {
                return resourceB.equals(resourceA) ||
                        getSuperClasses(getClass(resourceB)).contains(getClass(resourceA));
            }
        }
        return false;
    }

    public Set<OntClass> listSuperClasses(OntClass ontClass) {
        return modelCache.listSuperclasses(getResourceId(ontClass));
    }

    public Set<OntClass> listSubclasses(OntClass ontClass) {
        return modelCache.listSubclasses(getResourceId(ontClass));
    }

    public Collection<? extends OntClass> listSubclasses(Set<OntClass> classes) {
        return classes.stream()
                .map(this::listSubclasses)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Returns the list matched to the type of the template
     * If the list contains ontClass and the template is an Individual, it will return
     * the instances of the classes. If the list contains Individuals and the template
     * is a class it will return the Classes.
     */
    public Set<? extends OntResource> toType(Set<? extends OntResource> resources, OntResource template) {
        if (template instanceof Individual) {
            return toIndividuals(resources);
        } else if (template instanceof OntClass) {
            return toClasses(resources);
        } else {
            return resources;
        }
    }

    public void addFromJson(JsonObject references, ProgressIndicator indicator, boolean referenceDetails) {

        Set<String> strings = references.keySet();
        int total = strings.size();
        int processed = 0;
        indicator.setIndeterminate(false);
        indicator.setFraction(0d);
        for (String classType : strings) {
            indicator.setText(classType);
            OntClass ontClass = getClass(classType);
            if (ontClass == null) {
                return;
            }

            JsonElement jsonElement = references.get(classType);
            if (jsonElement.isJsonArray()) {
                Set<String> individuals = new HashSet<>();
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                jsonArray.forEach(element -> individuals.add(getSubject(element)));
                individuals.forEach(uri -> {
                    Individual individual = createIndividual(ontClass, uri);
                    if (referenceDetails) {
                        addReferenceDetails(jsonArray, individual);
                    }
                });
            }
            processed++;
            indicator.setFraction((double) processed / total);
        }
    }

    private String getSubject(JsonElement element) {
        if (element == null || !element.isJsonObject()) {
            return null;
        }
        JsonObject jsonStatement = element.getAsJsonObject();
        if (jsonStatement.has("s")) {
            return jsonStatement.get("s").getAsString();
        }
        return null;
    }

    private void addReferenceDetails(JsonArray array, Individual individual) {
        array.forEach(element -> {
            if (element != null && element.isJsonObject()) {
                JsonObject jsonStatement = element.getAsJsonObject();
                if (jsonStatement.has("s") && jsonStatement.has("p") && jsonStatement.has("o")) {
                    String subject = jsonStatement.get("s").getAsString();
                    if (subject.equals(getResourceId(individual))) {
                        String propertyIri = jsonStatement.get("p").getAsString();
                        String object = jsonStatement.get("o").getAsString();
                        Property property = getModel().getProperty(propertyIri);
                        // the individual is already added to a class, no need to add the rdf_type predicate again
                        if (!property.equals(OppModelConstants.RDF_TYPE)) {
                            individual.addProperty(property, object);
                        }
                    }
                }
            }
        });
    }

}
