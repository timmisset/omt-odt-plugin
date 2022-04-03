package com.misset.opp.ttl.util;

import com.misset.opp.ttl.OppModel;
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
                Arguments.of("string", OppModel.INSTANCE.XSD_STRING_INSTANCE),
                Arguments.of("boolean", OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE),
                Arguments.of("number", OppModel.INSTANCE.XSD_NUMBER_INSTANCE),
                Arguments.of("integer", OppModel.INSTANCE.XSD_INTEGER_INSTANCE),
                Arguments.of("decimal", OppModel.INSTANCE.XSD_DECIMAL_INSTANCE),
                Arguments.of("dateTime", OppModel.INSTANCE.XSD_DATETIME_INSTANCE),
                Arguments.of("date", OppModel.INSTANCE.XSD_DATE_INSTANCE),
                Arguments.of("void", OppModel.INSTANCE.VOID),
                Arguments.of("json", OppModel.INSTANCE.JSON_OBJECT),
                Arguments.of("unknown", null)
        );
    }

    private static Stream<Arguments> getClasses() {
        return Stream.of(
                Arguments.of("string", OppModel.INSTANCE.XSD_STRING),
                Arguments.of("boolean", OppModel.INSTANCE.XSD_BOOLEAN),
                Arguments.of("number", OppModel.INSTANCE.XSD_NUMBER),
                Arguments.of("integer", OppModel.INSTANCE.XSD_INTEGER),
                Arguments.of("decimal", OppModel.INSTANCE.XSD_DECIMAL),
                Arguments.of("dateTime", OppModel.INSTANCE.XSD_DATETIME),
                Arguments.of("date", OppModel.INSTANCE.XSD_DATE),
                Arguments.of("json", OppModel.INSTANCE.JSON),
                Arguments.of("unknown", null)
        );
    }

}
