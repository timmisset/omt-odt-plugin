package com.misset.opp.ttl.model;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper class to manage OppModel caching
 * Apache Jena is not the best when it comes to performance and multi-thread handling.
 * <p>
 * We register/cache 3 types of OntResources:
 * - OntClass: Ontology classes or data types
 * - Individual: Instances of ontology classes
 * - OntProperty: Ontology properties used as predicates in rdf statements
 */
public class OppModelCache {

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

    protected void flush() {
        cache.forEach(HashMap::clear);
    }

    /*
     * Individual
     */
    private final HashMap<String, Individual> individualMap = registerCacheEntity(new HashMap<>());
    private final HashMap<String, Set<Individual>> toIndividualsMap = registerCacheEntity(new HashMap<>());

    protected Individual getIndividual(String resourceId) {
        return individualMap.get(resourceId);
    }

    protected Set<Individual> toIndividuals(String resourceId) {
        return toIndividualsMap.getOrDefault(resourceId, new HashSet<>());
    }

    protected boolean isIndividual(String resourceId) {
        return getIndividual(resourceId) != null;
    }

    protected void cache(Individual individual) {
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

    protected OntClass getClass(String resourceId) {
        return classMap.get(resourceId);
    }

    protected Set<OntClass> getSuperclasses(String resourceId, boolean direct) {
        return direct ?
                superclassDirectMap.getOrDefault(resourceId, new HashSet<>()) :
                superclassMap.getOrDefault(resourceId, new HashSet<>());
    }

    protected Set<OntClass> getSubclasses(String resourceId, boolean direct) {
        return direct ?
                subclassDirectMap.getOrDefault(resourceId, new HashSet<>()) :
                subclassMap.getOrDefault(resourceId, new HashSet<>());
    }

    protected Set<OntClass> getClasses() {
        return new HashSet<>(classMap.values());
    }

    protected OntClass toClass(String resourceId) {
        return toClassMap.get(resourceId);
    }

    protected String toClassId(String resourceId) {
        return getResourceId(toClassMap.get(resourceId));
    }

    protected boolean isClass(String resourceId) {
        return getClass(resourceId) != null;
    }

    protected void cache(OntClass ontClass) {
        String resourceId = getResourceId(ontClass);
        classMap.put(resourceId, ontClass);
        toClassMap.put(resourceId, ontClass);
    }

    /**
     * Trigger caching the class tree, this will cache all superclass and subclass relationships
     * for every OntClass that has been cached while loading the model.
     * This method should be run AFTER the ontology has been fully loaded.
     */
    protected void cacheClassesTree() {
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
    protected void cacheSuperclasses(String uri, OntClass ontClass) {
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

    protected Set<OntClass> listSuperclasses(String uri) {
        return superclassMap.get(uri);
    }

    protected Set<OntClass> listSubclasses(String uri) {
        return subclassMap.get(uri);
    }

    /*
     * Property
     */
    private final HashMap<String, Property> propertyMap = registerCacheEntity(new HashMap<>());

    protected Property getProperty(String uri) {
        return propertyMap.get(uri);
    }

    protected Property getProperty(Resource resource) {
        return propertyMap.get(getResourceId(resource));
    }

    protected boolean isProperty(String uri) {
        return getProperty(uri) != null;
    }

    protected void cache(Property ontProperty) {
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

    protected void cache(OntClass subject, Property predicate, Resource object) {
        String subjectId = getResourceId(subject);
        String predicateId = getResourceId(predicate);
        String objectId = getResourceId(object);

        cache(subjectId, predicateId, objectId);
        cache(predicate);
    }

    protected void cache(String subjectId, String predicateId, String objectId) {
        addToSet(subjectId, predicateId, Set.of(objectId), subjectPredicateObjectsMap);
        addToSet(objectId, predicateId, Set.of(subjectId), objectPredicateSubjectsMap);
        addToSet(subjectId, Set.of(predicateId), subjectPredicateMap);
        addToSet(objectId, Set.of(predicateId), objectPredicateMap);
    }

    /**
     * Returns the predicates available on the provided resource
     */
    protected Set<Property> listSubjectPredicates(OntResource resource) {
        return subjectPredicateMap.getOrDefault(getResourceId(resource), new HashSet<>())
                .stream()
                .map(this::getProperty)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    protected Set<OntResource> listSubjects(Property predicate,
                                            OntResource object) {
        return getResources(objectPredicateSubjectsMap
                .getOrDefault(toClassId(getResourceId(object)), new HashMap<>())
                .getOrDefault(getResourceId(predicate), new HashSet<>()));
    }

    protected Set<OntResource> listObjects(Property predicate,
                                           OntResource subject) {
        return getResources(subjectPredicateObjectsMap
                .getOrDefault(toClassId(getResourceId(subject)), new HashMap<>())
                .getOrDefault(getResourceId(predicate), new HashSet<>()));
    }

    /**
     * Returns the predicates that point to the provided resource
     */
    protected Set<Property> listObjectPredicates(OntResource resource) {
        return objectPredicateMap.getOrDefault(getResourceId(resource), new HashSet<>())
                .stream()
                .map(this::getProperty)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
