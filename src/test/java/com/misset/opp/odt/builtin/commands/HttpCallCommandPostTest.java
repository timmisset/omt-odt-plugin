package com.misset.opp.odt.builtin.commands;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HttpCallCommandPostTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandPost.INSTANCE, OntologyModelConstants.getJsonObject());
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
    void testReturnsSecondArgument() {
        final Set<OntResource> secondReturnArgument = HttpCallCommandPost.INSTANCE.getSecondReturnArgument();
        Assertions.assertEquals(1, secondReturnArgument.size());
        Assertions.assertTrue(secondReturnArgument.contains(OntologyModelConstants.getXsdIntegerInstance()));
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
                OntologyModelConstants.getXsdStringInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandPost.INSTANCE,
                1,
                OntologyModelConstants.getJsonObject(),
                OntologyValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandPost.INSTANCE,
                2,
                OntologyModelConstants.getXsdBooleanInstance(),
                OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(HttpCallCommandPost.INSTANCE,
                3,
                OntologyModelConstants.getJsonObject(),
                OntologyValidationUtil.ERROR_MESSAGE_JSON);
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(HttpCallCommandPost.INSTANCE, 0, OntologyModelConstants.getXsdStringInstance());
        assertGetAcceptableArgumentType(HttpCallCommandPost.INSTANCE, 1, OntologyModelConstants.getJsonObject());
        assertGetAcceptableArgumentType(HttpCallCommandPost.INSTANCE, 2, OntologyModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentType(HttpCallCommandPost.INSTANCE, 3, OntologyModelConstants.getJsonObject());
        assertGetAcceptableArgumentTypeIsNull(HttpCallCommandPost.INSTANCE, 4);
    }
}
