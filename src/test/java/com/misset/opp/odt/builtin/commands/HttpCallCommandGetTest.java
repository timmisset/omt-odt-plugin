package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HttpCallCommandGetTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandGet.INSTANCE, OppModelConstants.JSON_OBJECT);
    }

    @Test
    void testName() {
        Assertions.assertEquals("HTTP_GET", HttpCallCommandGet.INSTANCE.getName());
    }

    @Test
    void testIsVoidReturnsFalse() {
        Assertions.assertFalse(HttpCallCommandGet.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, HttpCallCommandGet.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, HttpCallCommandGet.INSTANCE.maxNumberOfArguments());
    }

    @Test
    protected void testReturnsSecondArgument() {
        final Set<OntResource> secondReturnArgument = HttpCallCommandGet.INSTANCE.getSecondReturnArgument();
        Assertions.assertEquals(1, secondReturnArgument.size());
        Assertions.assertTrue(secondReturnArgument.contains(OppModelConstants.XSD_INTEGER_INSTANCE));
    }

    @Test
    void testArgumentTypes() {
        testArgument(HttpCallCommandGet.INSTANCE,
                0,
                OppModelConstants.XSD_STRING_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandGet.INSTANCE,
                1,
                OppModelConstants.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandGet.INSTANCE,
                2,
                OppModelConstants.XSD_BOOLEAN_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(HttpCallCommandGet.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentType(HttpCallCommandGet.INSTANCE, 1, OppModelConstants.JSON_OBJECT);
        assertGetAcceptableArgumentType(HttpCallCommandGet.INSTANCE, 2, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(HttpCallCommandGet.INSTANCE, 4);
    }
}
