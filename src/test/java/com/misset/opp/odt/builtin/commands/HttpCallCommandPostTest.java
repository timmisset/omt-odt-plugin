package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HttpCallCommandPostTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandPost.INSTANCE, OppModelConstants.JSON_OBJECT);
    }

    @Test
    void testName() {
        Assertions.assertEquals("HTTP_POST", HttpCallCommandPost.INSTANCE.getName());
    }

    @Test
    void testIsVoidReturnsFalse() {
        Assertions.assertFalse(HttpCallCommandPost.INSTANCE.isVoid());
    }

    @Test
    protected void testReturnsSecondArgument() {
        final Set<OntResource> secondReturnArgument = HttpCallCommandPost.INSTANCE.getSecondReturnArgument();
        Assertions.assertEquals(1, secondReturnArgument.size());
        Assertions.assertTrue(secondReturnArgument.contains(OppModelConstants.XSD_INTEGER_INSTANCE));
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, HttpCallCommandPost.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(4, HttpCallCommandPost.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testArgumentTypes() {
        testArgument(HttpCallCommandPost.INSTANCE,
                0,
                OppModelConstants.XSD_STRING_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandPost.INSTANCE,
                1,
                OppModelConstants.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandPost.INSTANCE,
                2,
                OppModelConstants.XSD_BOOLEAN_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(HttpCallCommandPost.INSTANCE,
                3,
                OppModelConstants.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(HttpCallCommandPost.INSTANCE, 0, OppModelConstants.XSD_STRING_INSTANCE);
        assertGetAcceptableArgumentType(HttpCallCommandPost.INSTANCE, 1, OppModelConstants.JSON_OBJECT);
        assertGetAcceptableArgumentType(HttpCallCommandPost.INSTANCE, 2, OppModelConstants.XSD_BOOLEAN_INSTANCE);
        assertGetAcceptableArgumentType(HttpCallCommandPost.INSTANCE, 3, OppModelConstants.JSON_OBJECT);
        assertGetAcceptableArgumentTypeIsNull(HttpCallCommandPost.INSTANCE, 4);
    }
}
