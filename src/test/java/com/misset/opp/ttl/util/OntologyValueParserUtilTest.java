package com.misset.opp.ttl.util;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValueParserUtil;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OntologyValueParserUtilTest {

    private static Stream<Arguments> getInstances() {
        return Stream.of(
                Arguments.of("string", OntologyModelConstants.getXsdStringInstance()),
                Arguments.of("boolean", OntologyModelConstants.getXsdBooleanInstance()),
                Arguments.of("number", OntologyModelConstants.getXsdNumberInstance()),
                Arguments.of("integer", OntologyModelConstants.getXsdIntegerInstance()),
                Arguments.of("decimal", OntologyModelConstants.getXsdDecimalInstance()),
                Arguments.of("dateTime", OntologyModelConstants.getXsdDatetimeInstance()),
                Arguments.of("date", OntologyModelConstants.getXsdDateInstance()),
                Arguments.of("void", OntologyModelConstants.getVoidResponse()),
                Arguments.of("json", OntologyModelConstants.getJsonObject()),
                Arguments.of("unknown", null)
        );
    }

    private static Stream<Arguments> getClasses() {
        return Stream.of(
                Arguments.of("string", OntologyModelConstants.getXsdString()),
                Arguments.of("boolean", OntologyModelConstants.getXsdBoolean()),
                Arguments.of("number", OntologyModelConstants.getXsdNumber()),
                Arguments.of("integer", OntologyModelConstants.getXsdInteger()),
                Arguments.of("decimal", OntologyModelConstants.getXsdDecimal()),
                Arguments.of("dateTime", OntologyModelConstants.getXsdDatetime()),
                Arguments.of("date", OntologyModelConstants.getXsdDate()),
                Arguments.of("json", OntologyModelConstants.getJson()),
                Arguments.of("unknown", null)
        );
    }

    @ParameterizedTest
    @MethodSource("getInstances")
    void testParsePrimitive(String primitive, Individual individual) {
        assertEquals(individual, OntologyValueParserUtil.parsePrimitive(primitive));
    }

    @ParameterizedTest
    @MethodSource("getClasses")
    void testParsePrimitiveClass(String className, OntClass ontClass) {
        assertEquals(ontClass, OntologyValueParserUtil.parsePrimitiveClass(className));
    }

}
