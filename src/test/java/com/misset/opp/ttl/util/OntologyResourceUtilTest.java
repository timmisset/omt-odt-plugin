package com.misset.opp.ttl.util;

import com.misset.opp.model.OntologyModel;
import com.misset.opp.model.util.OntologyResourceUtil;
import com.misset.opp.testcase.BasicTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OntologyResourceUtilTest {

    OntologyModel ontologyModel;

    @BeforeEach
    void setUp() {
        BasicTestCase.initOntologyModel();
        ontologyModel = OntologyModel.getInstance();
    }

    @ParameterizedTest
    @MethodSource("getCardinalityScenarios")
    void testGetCardinalityLabel(String classId, String propertyId, String expected) {
        String cardinalityLabel = OntologyResourceUtil.getCardinalityLabel(
                Set.of(ontologyModel.getClass(classId)),
                ontologyModel.getProperty(propertyId)
        );
        assertEquals(expected, cardinalityLabel);
    }

    private static Stream<Arguments> getCardinalityScenarios() {
        return Stream.of(
                Arguments.of("http://ontology#ClassA", "http://ontology#classPredicate", "1"),
                Arguments.of("http://ontology#ClassA", "http://ontology#booleanPredicate", "*"),
                Arguments.of("http://ontology#ClassC", "http://ontology#stringPredicate", "?"),
                Arguments.of("http://ontology#ClassC", "http://ontology#oneOrMore", "+")
        );
    }
}
