package com.misset.opp.ttl.util;

import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TTLValueParserUtilTest {

    @ParameterizedTest
    @MethodSource("getInstances")
    void testParsePrimitive(String primitive, Individual individual) {
        assertEquals(individual, TTLValueParserUtil.parsePrimitive(primitive));
    }

    @ParameterizedTest
    @MethodSource("getClasses")
    void testParsePrimitiveClass(String className, OntClass ontClass) {
        assertEquals(ontClass, TTLValueParserUtil.parsePrimitiveClass(className));
    }

    private static Stream<Arguments> getInstances() {
        return Stream.of(
                Arguments.of("string", OppModelConstants.XSD_STRING_INSTANCE),
                Arguments.of("boolean", OppModelConstants.XSD_BOOLEAN_INSTANCE),
                Arguments.of("number", OppModelConstants.XSD_NUMBER_INSTANCE),
                Arguments.of("integer", OppModelConstants.XSD_INTEGER_INSTANCE),
                Arguments.of("decimal", OppModelConstants.XSD_DECIMAL_INSTANCE),
                Arguments.of("dateTime", OppModelConstants.XSD_DATETIME_INSTANCE),
                Arguments.of("date", OppModelConstants.XSD_DATE_INSTANCE),
                Arguments.of("void", OppModelConstants.VOID),
                Arguments.of("json", OppModelConstants.JSON_OBJECT),
                Arguments.of("unknown", null)
        );
    }

    private static Stream<Arguments> getClasses() {
        return Stream.of(
                Arguments.of("string", OppModelConstants.XSD_STRING),
                Arguments.of("boolean", OppModelConstants.XSD_BOOLEAN),
                Arguments.of("number", OppModelConstants.XSD_NUMBER),
                Arguments.of("integer", OppModelConstants.XSD_INTEGER),
                Arguments.of("decimal", OppModelConstants.XSD_DECIMAL),
                Arguments.of("dateTime", OppModelConstants.XSD_DATETIME),
                Arguments.of("date", OppModelConstants.XSD_DATE),
                Arguments.of("json", OppModelConstants.JSON),
                Arguments.of("unknown", null)
        );
    }

}
