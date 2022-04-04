package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HttpCallCommandGetTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandGet.INSTANCE, oppModel.JSON_OBJECT);
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
        Assertions.assertTrue(secondReturnArgument.contains(OppModel.INSTANCE.XSD_INTEGER_INSTANCE));
    }

    @Test
    void testArgumentTypes() {
        testArgument(HttpCallCommandGet.INSTANCE,
                0,
                OppModel.INSTANCE.XSD_STRING_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandGet.INSTANCE,
                1,
                OppModel.INSTANCE.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandGet.INSTANCE,
                2,
                OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(HttpCallCommandGet.INSTANCE, 0, oppModel.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentType(HttpCallCommandGet.INSTANCE, 1, oppModel.JSON_OBJECT);
        assertGetAcceptableArgumentType(HttpCallCommandGet.INSTANCE, 2, oppModel.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentTypeIsNull(HttpCallCommandGet.INSTANCE, 4);
    }
}
