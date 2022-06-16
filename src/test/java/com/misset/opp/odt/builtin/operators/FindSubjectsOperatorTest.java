package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
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
        final OntClass classWithPredicateWithObject = oppModel.createClass("http://classWithPredicateWithObject");
        final OntClass classWithoutPredicate = oppModel.createClass("http://classWithoutPredicate");
        final OntClass classWithPredicateWithoutObject = oppModel.createClass("http://classWithPredicateWithoutObject");
        final OntProperty property = oppModel.createProperty("http://property");
        oppModel.createStatement(classWithPredicateWithObject, property, OppModelConstants.XSD_INTEGER_INSTANCE);
        oppModel.createStatement(classWithPredicateWithoutObject, property, OppModelConstants.XSD_BOOLEAN_INSTANCE);

        final OntResource individualWithClass = oppModel.createIndividual(classWithPredicateWithObject);
        final Set<OntResource> individuals = Set.of(individualWithClass,
                oppModel.createIndividual(classWithPredicateWithoutObject),
                oppModel.createIndividual(classWithoutPredicate));

        // return a class that has the property and object (xsd_integer_instance)
        assertResolved(FindSubjectsOperator.INSTANCE, individuals, Set.of(individualWithClass), Set.of(property), Set.of(OppModelConstants.XSD_INTEGER_INSTANCE));
    }

    @Test
    void testValidArguments() {
        assertValidArgument(FindSubjectsOperator.INSTANCE, 2, OppModelConstants.NAMED_GRAPH);
        assertInvalidArgument(FindSubjectsOperator.INSTANCE, 2, OppModelConstants.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
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
        assertGetAcceptableArgumentType(FindSubjectsOperator.INSTANCE, 2, OppModelConstants.NAMED_GRAPH);
        assertGetAcceptableArgumentTypeIsNull(FindSubjectsOperator.INSTANCE, 1);
    }

    @Test
    void testReturnsEmptyListWhenNoArguments() {
        Assertions.assertEquals(0,
                FindSubjectsOperator.INSTANCE.resolveFrom(Collections.emptySet(), getCall()).size());
    }
}
