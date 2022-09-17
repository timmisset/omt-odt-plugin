package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

class FindSubjectsOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        final OntClass classWithPredicateWithObject = ontologyModel.createClass("http://classWithPredicateWithObject");
        final OntClass classWithoutPredicate = ontologyModel.createClass("http://classWithoutPredicate");
        final OntClass classWithPredicateWithoutObject = ontologyModel.createClass("http://classWithPredicateWithoutObject");
        final OntProperty property = ontologyModel.createProperty("http://property");
        ontologyModel.createStatement(classWithPredicateWithObject, property, OntologyModelConstants.getXsdIntegerInstance());
        ontologyModel.createStatement(classWithPredicateWithoutObject, property, OntologyModelConstants.getXsdBooleanInstance());

        final OntResource individualWithClass = ontologyModel.createIndividual(classWithPredicateWithObject);
        final Set<OntResource> individuals = Set.of(individualWithClass,
                ontologyModel.createIndividual(classWithPredicateWithoutObject),
                ontologyModel.createIndividual(classWithoutPredicate));

        // return a class that has the property and object (xsd_integer_instance)
        assertResolved(FindSubjectsOperator.INSTANCE, individuals, Set.of(individualWithClass), Set.of(property), Set.of(OntologyModelConstants.getXsdIntegerInstance()));
    }

    @Test
    void testValidArguments() {
        assertValidArgument(FindSubjectsOperator.INSTANCE, 2, OntologyModelConstants.getNamedGraph());
        assertInvalidArgument(FindSubjectsOperator.INSTANCE, 2, OntologyModelConstants.getXsdIntegerInstance(), OntologyValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
    }

    @Test
    void testName() {
        Assertions.assertEquals("FIND_SUBJECTS", FindSubjectsOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, FindSubjectsOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, FindSubjectsOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(FindSubjectsOperator.INSTANCE, 2, OntologyModelConstants.getNamedGraph());
        assertGetAcceptableArgumentTypeIsNull(FindSubjectsOperator.INSTANCE, 1);
    }

    @Test
    void testReturnsEmptyListWhenNoArguments() {
        Assertions.assertEquals(0,
                FindSubjectsOperator.INSTANCE.resolveFrom(Collections.emptySet(), getCall()).size());
    }
}
