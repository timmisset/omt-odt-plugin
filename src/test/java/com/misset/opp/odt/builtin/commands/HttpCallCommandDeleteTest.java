package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HttpCallCommandDeleteTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandDelete.INSTANCE, OntologyModelConstants.getJsonObject());
    }

    @Test
    void testName() {
        Assertions.assertEquals("HTTP_DELETE", HttpCallCommandDelete.INSTANCE.getName());
    }

    @Test
    void testIsVoidReturnsFalse() {
        Assertions.assertFalse(HttpCallCommandDelete.INSTANCE.isVoid());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(1, HttpCallCommandDelete.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(4, HttpCallCommandDelete.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testReturnsSecondArgument() {
        final Set<OntResource> secondReturnArgument = HttpCallCommandDelete.INSTANCE.getSecondReturnArgument();
        Assertions.assertEquals(1, secondReturnArgument.size());
        Assertions.assertTrue(secondReturnArgument.contains(OntologyModelConstants.getXsdIntegerInstance()));
    }

    @Test
    void testArgumentTypes() {
        testArgument(HttpCallCommandDelete.INSTANCE,
                0,
                OntologyModelConstants.getXsdStringInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandDelete.INSTANCE,
                1,
                OntologyModelConstants.getJsonObject(),
                OntologyValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandDelete.INSTANCE,
                2,
                OntologyModelConstants.getXsdBooleanInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(HttpCallCommandDelete.INSTANCE,
                3,
                OntologyModelConstants.getJsonObject(),
                OntologyValidationUtil.ERROR_MESSAGE_JSON);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(HttpCallCommandDelete.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentType(HttpCallCommandDelete.INSTANCE, 1, OntologyModelConstants.getJsonObject());
        assertGetAcceptableArgumentType(HttpCallCommandDelete.INSTANCE, 2, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentType(HttpCallCommandDelete.INSTANCE, 3, OntologyModelConstants.getJsonObject());
        assertGetAcceptableArgumentTypeIsNull(HttpCallCommandDelete.INSTANCE, 4);
    }
}
