package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQuery;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilterOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FilterOperator.INSTANCE, OppModelConstants.getXsdBooleanInstance(), OppModelConstants.getXsdBooleanInstance());
        assertResolved(FilterOperator.INSTANCE, OppModelConstants.getXsdDate(), OppModelConstants.getXsdDate());
        assertResolved(FilterOperator.INSTANCE, OppModelConstants.getXsdInteger(), OppModelConstants.getXsdInteger());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(FilterOperator.INSTANCE, 0, OppModelConstants.getXsdBooleanInstance());
        assertValidArgument(FilterOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance());
    }

    @Test
    void testName() {
        assertEquals("FILTER", FilterOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        assertEquals(1, FilterOperator.INSTANCE.minNumberOfArguments());
        assertEquals(1, FilterOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(FilterOperator.INSTANCE, 0, Set.of(OppModelConstants.getXsdBooleanInstance(), OppModelConstants.getXsdIntegerInstance()));
    }

    @Test
    void testFiltersByNumber() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdIntegerInstance()));
        Set<OntResource> resources = Set.of(OppModelConstants.getXsdStringInstance());
        Set<OntResource> filtered = FilterOperator.INSTANCE.resolveFrom(resources, call);
        assertEquals(resources, filtered);
    }

    @Test
    void testFiltersByBoolean() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdBooleanInstance()));
        ODTSignatureArgument signatureArgument = mock(ODTSignatureArgument.class);
        ODTResolvableValue resolvableValue = mock(ODTResolvableValue.class);
        ODTResolvableQuery query = mock(ODTResolvableQuery.class);
        doReturn(signatureArgument).when(call).getCallSignatureArgumentElement(0);
        doReturn(resolvableValue).when(signatureArgument).getResolvableValue();
        doReturn(query).when(resolvableValue).getQuery();
        doReturn(true).when(query).isBoolean();
        Set<OntResource> resources = Set.of(OppModelConstants.getXsdStringInstance(), OppModelConstants.getXsdDate());

        doReturn(Set.of(OppModelConstants.getXsdStringInstance())).when(query).filter(resources);

        Set<OntResource> filtered = FilterOperator.INSTANCE.resolveFrom(resources, call);
        assertNotEquals(resources, filtered);
        assertTrue(filtered.stream().noneMatch(resource -> resource.equals(OppModelConstants.getXsdDate())));
        assertTrue(filtered.stream().allMatch(resource -> resource.equals(OppModelConstants.getXsdStringInstance())));

        verify(query).filter(resources);
    }
}
