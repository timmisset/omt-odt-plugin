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
                Arguments.of("string", OppModelConstants.getXsdStringInstance()),
                Arguments.of("boolean", OppModelConstants.getXsdBooleanInstance()),
                Arguments.of("number", OppModelConstants.getXsdNumberInstance()),
                Arguments.of("integer", OppModelConstants.getXsdIntegerInstance()),
                Arguments.of("decimal", OppModelConstants.getXsdDecimalInstance()),
                Arguments.of("dateTime", OppModelConstants.getXsdDatetimeInstance()),
                Arguments.of("date", OppModelConstants.getXsdDateInstance()),
                Arguments.of("void", OppModelConstants.getVoidResponse()),
                Arguments.of("json", OppModelConstants.getJsonObject()),
                Arguments.of("unknown", null)
        );
    }

    private static Stream<Arguments> getClasses() {
        return Stream.of(
                Arguments.of("string", OppModelConstants.getXsdString()),
                Arguments.of("boolean", OppModelConstants.getXsdBoolean()),
                Arguments.of("number", OppModelConstants.getXsdNumber()),
                Arguments.of("integer", OppModelConstants.getXsdInteger()),
                Arguments.of("decimal", OppModelConstants.getXsdDecimal()),
                Arguments.of("dateTime", OppModelConstants.getXsdDatetime()),
                Arguments.of("date", OppModelConstants.getXsdDate()),
                Arguments.of("json", OppModelConstants.getJson()),
                Arguments.of("unknown", null)
        );
    }

}
