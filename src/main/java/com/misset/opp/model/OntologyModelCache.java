package com.misset.opp.model;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper class to manage OntologyModel caching
 * Apache Jena is not the best when it comes to performance and multi-thread handling.
 * <p>
 * We register/cache 3 types of OntResources:
 * - OntClass: Ontology classes or data types
 * - Individual: Instances of ontology classes
 * - OntProperty: Ontology properties used as predicates in rdf statements
 */
@Service
public final class OntologyModelCache {

    public OntologyModelCache() {
        cacheConstants();
    }

    public static OntologyModelCache getInstance(Project project) {
        return project.getService(OntologyModelCache.class);
    }

    /*
     * Cache register
     */
    List<HashMap<?, ?>> cache = new ArrayList<>();

    private <K, T> HashMap<K, T> registerCacheEntity(HashMap<K, T> cacheEntity) {
        cache.add(cacheEntity);
        return cacheEntity;
    }

    private <K, T> void addToSet(K key, Set<T> value, HashMap<K, Set<T>> map) {
        Set<T> set = map.getOrDefault(key, new HashSet<>());
        set.addAll(value);
        map.put(key, set);
    }

    private <K, T> void addToSet(K key, K key2, Set<T> value, HashMap<K, HashMap<K, Set<T>>> map) {
        HashMap<K, Set<T>> root = map.getOrDefault(key, new HashMap<>());
        addToSet(key2, value, root);
        map.put(key, root);
    }

    private String getResourceId(Resource resource) {
        return Optional.ofNullable(resource.getURI()).orElseGet(() -> resource.getId().toString());
    }

    private OntResource getResource(String resourceId) {
        if (isIndividual(resourceId)) {
            return getIndividual(resourceId);
        }
        if (isClass(resourceId)) {
            return getClass(resourceId);
        }
        return null;
    }

    private Set<OntResource> getResources(Set<String> resourceIds) {
        return resourceIds.stream()
                .map(this::getResource)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    void flush() {
        cache.forEach(HashMap::clear);
        cacheConstants();
    }

    /*
     * Individual
     */
    private final HashMap<String, Individual> individualMap = registerCacheEntity(new HashMap<>());
    private final HashMap<String, Set<Individual>> toIndividualsMap = registerCacheEntity(new HashMap<>());

    Individual getIndividual(String resourceId) {
        return individualMap.get(resourceId);
    }

    Set<Individual> toIndividuals(String resourceId) {
        return toIndividualsMap.getOrDefault(resourceId, new HashSet<>());
    }

    boolean isIndividual(String resourceId) {
        return getIndividual(resourceId) != null;
    }

    void cacheIndividual(Individual individual) {
        String resourceId = getResourceId(individual);
        individualMap.put(resourceId, individual);
        toIndividualsMap.put(resourceId, Set.of(individual));

        cacheIndividualToClass(getResourceId(individual.getOntClass()), individual);
    }

    private void cacheIndividualToClass(String classResourceId, Individual individual) {
        OntClass ontClass = getClass(classResourceId);
        String individualUri = getResourceId(individual);
        if (ontClass == null) {
            return;
        }
        toClassMap.put(individualUri, ontClass);

        // add the individual as an instance of the class
        addToSet(classResourceId, Set.of(individual), toIndividualsMap);
    }

    /*
     * OntClass
     */
    // classMap contains all available OntClass members of the model
    private final HashMap<String, OntClass> classMap = registerCacheEntity(new HashMap<>());
    // toClassMap contains all translations from individuals/class uris -> ontClasses
    private final HashMap<String, OntClass> toClassMap = registerCacheEntity(new HashMap<>());
    private final HashMap<String, Set<OntClass>> superclassDirectMap = registerCacheEntity(new HashMap<>());
    private final HashMap<String, Set<OntClass>> superclassMap = registerCacheEntity(new HashMap<>());
    private final HashMap<String, Set<OntClass>> subclassDirectMap = registerCacheEntity(new HashMap<>());
    private final HashMap<String, Set<OntClass>> subclassMap = registerCacheEntity(new HashMap<>());

    OntClass getClass(String resourceId) {
        return classMap.get(resourceId);
    }

    Set<OntClass> getSuperclasses(String resourceId, boolean direct) {
        return direct ?
                superclassDirectMap.getOrDefault(resourceId, new HashSet<>()) :
                superclassMap.getOrDefault(resourceId, new HashSet<>());
    }

    Set<OntClass> getSubclasses(String resourceId, boolean direct) {
        return direct ?
                subclassDirectMap.getOrDefault(resourceId, new HashSet<>()) :
                subclassMap.getOrDefault(resourceId, new HashSet<>());
    }

    Set<OntClass> getClasses() {
        return new HashSet<>(classMap.values());
    }

    OntClass toClass(String resourceId) {
        return toClassMap.get(resourceId);
    }

    String toClassId(String resourceId) {
        return getResourceId(toClassMap.get(resourceId));
    }

    boolean isClass(String resourceId) {
        return getClass(resourceId) != null;
    }

    void cacheClass(OntClass ontClass) {
        String resourceId = getResourceId(ontClass);
        classMap.put(resourceId, ontClass);
        toClassMap.put(resourceId, ontClass);
    }

    /**
     * Trigger caching the class tree, this will cache all superclass and subclass relationships
     * for every OntClass that has been cached while loading the model.
     * This method should be run AFTER the ontology has been fully loaded.
     */
    void cacheClassesTree() {
        classMap.values().forEach(ontClass -> {
            String classResourceId = getResourceId(ontClass);
            cacheSuperclasses(classResourceId, ontClass);
            cacheSubclasses(classResourceId, ontClass);

            Set<OntClass> superClasses = superclassMap.getOrDefault(classResourceId, new HashSet<>());
            superClasses.stream().map(this::getResourceId)
                    .forEach(superClassId -> {
                        // set all the predicates based on ones own predicates and superclass predicates:
                        addToSet(classResourceId, subjectPredicateMap.getOrDefault(superClassId, new HashSet<>()), subjectPredicateMap);
                        addToSet(classResourceId, objectPredicateMap.getOrDefault(superClassId, new HashSet<>()), objectPredicateMap);
                        cacheSPOMaps(superClassId, classResourceId, subjectPredicateObjectsMap);
                        cacheSPOMaps(superClassId, classResourceId, objectPredicateSubjectsMap);
                    });
        });
    }

    private void cacheSPOMaps(String superclassId,
                              String classResourceId,
                              HashMap<String, HashMap<String, Set<String>>> map) {
        HashMap<String, Set<String>> superclassSP = map.getOrDefault(superclassId, new HashMap<>());
        superclassSP.keySet().forEach(
                predicate -> {
                    Set<String> superclassPO = superclassSP.get(predicate);
                    addToSet(classResourceId, predicate, superclassPO, map);
                }
        );
    }

    /**
     * Cache the direct superclasses of the provided ontClass. The full list of superclasses
     * will be generated on the fly since it requires the full ontology to be loaded first
     */
    void cacheSuperclasses(String uri, OntClass ontClass) {
        if (superclassDirectMap.containsKey(uri)) {
            return;
        }
        superclassDirectMap.put(uri, ontClass.listSuperClasses(true).toSet());
        superclassMap.put(uri, ontClass.listSuperClasses(false).toSet());
    }

    private void cacheSubclasses(String uri, OntClass ontClass) {
        if (subclassDirectMap.containsKey(uri)) {
            return;
        }
        subclassDirectMap.put(uri, ontClass.listSubClasses(true).toSet());
        subclassMap.put(uri, ontClass.listSubClasses(false).toSet());
    }

    Set<OntClass> listSuperclasses(String uri) {
        return superclassMap.getOrDefault(uri, new HashSet<>());
    }

    Set<OntClass> listSubclasses(String uri) {
        return subclassMap.getOrDefault(uri, new HashSet<>());
    }

    /*
     * Property
     */
    private final HashMap<String, Property> propertyMap = registerCacheEntity(new HashMap<>());

    Property getProperty(String uri) {
        return propertyMap.get(uri);
    }

    Property getProperty(Resource resource) {
        return propertyMap.get(getResourceId(resource));
    }

    boolean isProperty(String uri) {
        return getProperty(uri) != null;
    }

    void cacheClass(Property ontProperty) {
        propertyMap.put(getResourceId(ontProperty), ontProperty);
    }

    /*
     * Statements
     * Used to describe the relationship between subject-predicate-object
     * These are cached to quickly list all predicates or predicate-objects for a class, individual etc
     */
    private final HashMap<String, HashMap<String, Set<String>>> subjectPredicateObjectsMap = registerCacheEntity(new HashMap<>());
    private final HashMap<String, HashMap<String, Set<String>>> objectPredicateSubjectsMap = registerCacheEntity(new HashMap<>());
    private final HashMap<String, Set<String>> subjectPredicateMap = registerCacheEntity(new HashMap<>());
    private final HashMap<String, Set<String>> objectPredicateMap = registerCacheEntity(new HashMap<>());

    void cacheSubjectPredicateObject(OntClass subject, Property predicate, Resource object) {
        String subjectId = getResourceId(subject);
        String predicateId = getResourceId(predicate);
        String objectId = getResourceId(object);

        cacheSubjectPredicateObjectIds(subjectId, predicateId, objectId);
        cacheClass(predicate);
    }

    private void cacheSubjectPredicateObjectIds(String subjectId, String predicateId, String objectId) {
        addToSet(subjectId, predicateId, Set.of(objectId), subjectPredicateObjectsMap);
        addToSet(objectId, predicateId, Set.of(subjectId), objectPredicateSubjectsMap);
        addToSet(subjectId, Set.of(predicateId), subjectPredicateMap);
        addToSet(objectId, Set.of(predicateId), objectPredicateMap);
    }

    /**
     * Returns the predicates available on the provided resource
     */
    Set<Property> listSubjectPredicates(OntResource resource) {
        return subjectPredicateMap.getOrDefault(getResourceId(resource), new HashSet<>())
                .stream()
                .map(this::getProperty)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    Set<OntResource> listSubjects(Property predicate,
                                  OntResource object) {
        return getResources(objectPredicateSubjectsMap
                .getOrDefault(toClassId(getResourceId(object)), new HashMap<>())
                .getOrDefault(getResourceId(predicate), new HashSet<>()));
    }

    Set<OntResource> listObjects(Property predicate,
                                 OntResource subject) {
        return getResources(subjectPredicateObjectsMap
                .getOrDefault(toClassId(getResourceId(subject)), new HashMap<>())
                .getOrDefault(getResourceId(predicate), new HashSet<>()));
    }

    /**
     * Returns the predicates that point to the provided resource
     */
    Set<Property> listObjectPredicates(OntResource resource) {
        return objectPredicateMap.getOrDefault(getResourceId(resource), new HashSet<>())
                .stream()
                .map(this::getProperty)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    /**
     * If only a single resource contains the given property, it will return that resource
     * if zero or more than 1 contain property, it will return null
     */
    OntResource getUnambigiousResource(Property property) {
        List<OntResource> resources = subjectPredicateMap.entrySet()
                .stream().filter(
                        stringSetEntry -> stringSetEntry.getValue().contains(getResourceId(property))
                )
                .map(Map.Entry::getKey)
                .map(this::getResource)
                .collect(Collectors.toList());
        if (resources.size() == 1) {
            return resources.get(0);
        }
        return null;
    }

    public Set<Individual> getIndividuals() {
        return new HashSet<>(individualMap.values());
    }

    private void cacheConstants() {
        OntModel ontModel = OntologyModelConstants.getOntModel();
        ontModel.listClasses().forEach(this::cacheClass);
        ontModel.listIndividuals().forEach(this::cacheIndividual);
        ontModel.listOntProperties().forEach(this::cacheClass);
    }
}
