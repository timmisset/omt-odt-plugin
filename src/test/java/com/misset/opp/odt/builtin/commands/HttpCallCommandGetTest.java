package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HttpCallCommandGetTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandGet.INSTANCE, OntologyModelConstants.getJsonObject());
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
    void testReturnsSecondArgument() {
        final Set<OntResource> secondReturnArgument = HttpCallCommandGet.INSTANCE.getSecondReturnArgument();
        Assertions.assertEquals(1, secondReturnArgument.size());
        Assertions.assertTrue(secondReturnArgument.contains(OntologyModelConstants.getXsdIntegerInstance()));
    }

    @Test
    void testArgumentTypes() {
        testArgument(HttpCallCommandGet.INSTANCE,
                0,
                OntologyModelConstants.getXsdStringInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandGet.INSTANCE,
                1,
                OntologyModelConstants.getJsonObject(),
                OntologyValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandGet.INSTANCE,
                2,
                OntologyModelConstants.getXsdBooleanInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(HttpCallCommandGet.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentType(HttpCallCommandGet.INSTANCE, 1, OntologyModelConstants.getJsonObject());
        assertGetAcceptableArgumentType(HttpCallCommandGet.INSTANCE, 2, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeIsNull(HttpCallCommandGet.INSTANCE, 4);
    }
}
