package com.misset.opp.callable.builtin.commands;

import com.misset.opp.callable.builtin.BuiltInTest;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.validation.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class HttpCallCommandPostTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandPost.INSTANCE, oppModel.JSON_OBJECT);
    }

    @Test
    protected void testReturnsSecondArgument() {
        final Set<OntResource> secondReturnArgument = HttpCallCommandPost.INSTANCE.getSecondReturnArgument();
        Assertions.assertEquals(1, secondReturnArgument.size());
        Assertions.assertTrue(secondReturnArgument.contains(OppModel.INSTANCE.XSD_INTEGER_INSTANCE));
    }

    @Test
    void testArgumentTypes() {
        testArgument(HttpCallCommandPost.INSTANCE,
                0,
                OppModel.INSTANCE.XSD_STRING_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandPost.INSTANCE,
                1,
                OppModel.INSTANCE.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandPost.INSTANCE,
                2,
                OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(HttpCallCommandPost.INSTANCE,
                3,
                OppModel.INSTANCE.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
    }
}
