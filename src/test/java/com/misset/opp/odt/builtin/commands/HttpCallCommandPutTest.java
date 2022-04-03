package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.mock;

class HttpCallCommandPutTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturns(HttpCallCommandPut.INSTANCE, oppModel.JSON_OBJECT);
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
        Assertions.assertTrue(secondReturnArgument.contains(OppModel.INSTANCE.XSD_INTEGER_INSTANCE));
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
                OppModel.INSTANCE.XSD_STRING_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_STRING);
        testArgument(HttpCallCommandPut.INSTANCE,
                1,
                OppModel.INSTANCE.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
        testArgument(HttpCallCommandPut.INSTANCE,
                2,
                OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE,
                TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
        testArgument(HttpCallCommandPut.INSTANCE,
                3,
                OppModel.INSTANCE.JSON_OBJECT,
                TTLValidationUtil.ERROR_MESSAGE_JSON);
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsStringForIndex0() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = HttpCallCommandPut.INSTANCE.getAcceptableArgumentTypeWithContext(0, call);
        Assertions.assertEquals(1, acceptableArgumentTypeWithContext.size());
        Assertions.assertEquals(OppModel.INSTANCE.XSD_STRING_INSTANCE, acceptableArgumentTypeWithContext.iterator().next());
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsJsonForIndex1() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = HttpCallCommandPut.INSTANCE.getAcceptableArgumentTypeWithContext(1, call);
        Assertions.assertEquals(1, acceptableArgumentTypeWithContext.size());
        Assertions.assertEquals(OppModel.INSTANCE.JSON_OBJECT, acceptableArgumentTypeWithContext.iterator().next());
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsBooleanForIndex2() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = HttpCallCommandPut.INSTANCE.getAcceptableArgumentTypeWithContext(2, call);
        Assertions.assertEquals(1, acceptableArgumentTypeWithContext.size());
        Assertions.assertEquals(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE, acceptableArgumentTypeWithContext.iterator().next());
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsJsonForIndex3() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = HttpCallCommandPut.INSTANCE.getAcceptableArgumentTypeWithContext(3, call);
        Assertions.assertEquals(1, acceptableArgumentTypeWithContext.size());
        Assertions.assertEquals(OppModel.INSTANCE.JSON_OBJECT, acceptableArgumentTypeWithContext.iterator().next());
    }

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsNull() {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = HttpCallCommandPut.INSTANCE.getAcceptableArgumentTypeWithContext(4, call);
        Assertions.assertNull(acceptableArgumentTypeWithContext);
    }
}
