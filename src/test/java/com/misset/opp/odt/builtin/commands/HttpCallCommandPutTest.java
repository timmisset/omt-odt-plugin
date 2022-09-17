package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HttpCallCommandPutTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandPut.INSTANCE, OntologyModelConstants.getJsonObject());
    }

    @Test
    void testName() {
        Assertions.assertEquals("HTTP_PUT", HttpCallCommandPut.INSTANCE.getName());
    }

    @Test
    void testIsVoidReturnsFalse() {
        Assertions.assertFalse(HttpCallCommandPut.INSTANCE.isVoid());
    }

    @Test
    void testReturnsSecondArgument() {
        final Set<OntResource> secondReturnArgument = HttpCallCommandPut.INSTANCE.getSecondReturnArgument();
        Assertions.assertEquals(1, secondReturnArgument.size());
        Assertions.assertTrue(secondReturnArgument.contains(OntologyModelConstants.getXsdIntegerInstance()));
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, HttpCallCommandPut.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(4, HttpCallCommandPut.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testArgumentTypes() {
        testArgument(HttpCallCommandPut.INSTANCE,
                0,
                OntologyModelConstants.getXsdStringInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandPut.INSTANCE,
                1,
                OntologyModelConstants.getJsonObject(),
                OntologyValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandPut.INSTANCE,
                2,
                OntologyModelConstants.getXsdBooleanInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(HttpCallCommandPut.INSTANCE,
                3,
                OntologyModelConstants.getJsonObject(),
                OntologyValidationUtil.ERROR_MESSAGE_JSON);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(HttpCallCommandPut.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentType(HttpCallCommandPut.INSTANCE, 1, OntologyModelConstants.getJsonObject());
        assertGetAcceptableArgumentType(HttpCallCommandPut.INSTANCE, 2, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentType(HttpCallCommandPut.INSTANCE, 3, OntologyModelConstants.getJsonObject());
        assertGetAcceptableArgumentTypeIsNull(HttpCallCommandPut.INSTANCE, 4);
    }
}
