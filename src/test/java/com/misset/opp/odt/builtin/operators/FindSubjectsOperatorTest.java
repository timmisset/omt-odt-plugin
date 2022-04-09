package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
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
        final OntModel model = oppModel.getModel();
        final OntClass classWithPredicate = model.createClass("http://ClassWithPredicate");
        final OntClass classWithoutPredicate = model.createClass("http://ClassWithoutPredicate");
        final OntProperty property = model.createOntProperty("http://property");
        classWithPredicate.addProperty(property, "");

        final OntResource individualWithClass = classWithPredicate.createIndividual();
        final Set<OntResource> individuals = Set.of(individualWithClass,
                classWithoutPredicate.createIndividual());
        assertResolved(FindSubjectsOperator.INSTANCE, individuals, Set.of(individualWithClass), Set.of(property));
    }

    @Test
    protected void testResolveWithObjects() {
        final OntModel model = oppModel.getModel();
        final OntClass classWithPredicateWithObject = model.createClass("http://classWithPredicateWithObject");
        final OntClass classWithoutPredicate = model.createClass("http://classWithoutPredicate");
        final OntClass classWithPredicateWithoutObject = model.createClass("http://classWithPredicateWithoutObject");
        final OntProperty property = model.createOntProperty("http://property");
        classWithPredicateWithObject.addProperty(property, oppModel.XSD_INTEGER_INSTANCE);
        classWithPredicateWithoutObject.addProperty(property, oppModel.XSD_BOOLEAN_INSTANCE);

        final OntResource individualWithClass = classWithPredicateWithObject.createIndividual();
        final Set<OntResource> individuals = Set.of(individualWithClass,
                classWithPredicateWithoutObject.createIndividual(),
                classWithoutPredicate.createIndividual());

        // return a class that has the property and object (xsd_integer_instance)
        assertResolved(FindSubjectsOperator.INSTANCE, individuals, Set.of(individualWithClass), Set.of(property), Set.of(oppModel.XSD_INTEGER_INSTANCE));
    }

    @Test
    void testValidArguments() {
        assertValidArgument(FindSubjectsOperator.INSTANCE, 2, oppModel.NAMED_GRAPH);
        assertInvalidArgument(FindSubjectsOperator.INSTANCE, 2, oppModel.XSD_INTEGER_INSTANCE, TTLValidationUtil.ERROR_MESSAGE_NAMED_GRAPH);
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
        assertGetAcceptableArgumentType(FindSubjectsOperator.INSTANCE, 2, oppModel.NAMED_GRAPH);
        assertGetAcceptableArgumentTypeIsNull(FindSubjectsOperator.INSTANCE, 1);
    }

    @Test
    void testReturnsEmptyListWhenNoArguments() {
        Assertions.assertEquals(0,
                FindSubjectsOperator.INSTANCE.resolveFrom(Collections.emptySet(), getCall()).size());
    }
}
