package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HttpCallCommandPutTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandPut.INSTANCE, OppModelConstants.JSON_OBJECT);
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
    protected void testReturnsSecondArgument() {
        final Set<OntResource> secondReturnArgument = HttpCallCommandPut.INSTANCE.getSecondReturnArgument();
        Assertions.assertEquals(1, secondReturnArgument.size());
        Assertions.assertTrue(secondReturnArgument.contains(OppModelConstants.XSD_INTEGER_INSTANCE));
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
                OppModelConstants.XSD_STRING_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandPut.INSTANCE,
                1,
                OppModelConstants.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandPut.INSTANCE,
                2,
                OppModelConstants.XSD_BOOLEAN_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(HttpCallCommandPut.INSTANCE,
                3,
                OppModelConstants.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(HttpCallCommandPut.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentType(HttpCallCommandPut.INSTANCE, 1, OppModelConstants.JSON_OBJECT);
        assertGetAcceptableArgumentType(HttpCallCommandPut.INSTANCE, 2, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentType(HttpCallCommandPut.INSTANCE, 3, OppModelConstants.JSON_OBJECT);
        assertGetAcceptableArgumentTypeIsNull(HttpCallCommandPut.INSTANCE, 4);
    }
}
