package com.misset.opp.odt.builtin.operators;

import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.odt.psi.ODTQuery;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilterOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(FilterOperator.INSTANCE, OntologyModelConstants.getXsdBooleanInstance(), OntologyModelConstants.getXsdBooleanInstance());
        assertResolved(FilterOperator.INSTANCE, OntologyModelConstants.getXsdDate(), OntologyModelConstants.getXsdDate());
        assertResolved(FilterOperator.INSTANCE, OntologyModelConstants.getXsdInteger(), OntologyModelConstants.getXsdInteger());
    }

    @Test
    void testValidArguments() {
        assertValidArgument(FilterOperator.INSTANCE, 0, OntologyModelConstants.getXsdBooleanInstance());
        assertValidArgument(FilterOperator.INSTANCE, 0, OntologyModelConstants.getXsdIntegerInstance());
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
        assertGetAcceptableArgumentType(FilterOperator.INSTANCE, 0, Set.of(OntologyModelConstants.getXsdBooleanInstance(), OntologyModelConstants.getXsdIntegerInstance()));
    }

    @Test
    void testFiltersByNumber() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdIntegerInstance()));
        Set<OntResource> resources = Set.of(OntologyModelConstants.getXsdStringInstance());
        Set<OntResource> filtered = FilterOperator.INSTANCE.resolveFrom(resources, call);
        assertEquals(resources, filtered);
    }

    @Test
    void testFiltersByBoolean() {
        PsiCall call = getCall(Set.of(OntologyModelConstants.getXsdBooleanInstance()));
        ODTSignatureArgument signatureArgument = mock(ODTSignatureArgument.class);
        ODTResolvableValue resolvableValue = mock(ODTResolvableValue.class);
        ODTQuery query = mock(ODTQuery.class);
        doReturn(signatureArgument).when(call).getCallSignatureArgumentElement(0);
        doReturn(resolvableValue).when(signatureArgument).getResolvableValue();
        doReturn(query).when(resolvableValue).getQuery();
        doReturn(true).when(query).isBoolean();
        Set<OntResource> resources = Set.of(OntologyModelConstants.getXsdStringInstance(), OntologyModelConstants.getXsdDate());

        doReturn(Set.of(OntologyModelConstants.getXsdStringInstance())).when(query).filter(resources);

        Set<OntResource> filtered = FilterOperator.INSTANCE.resolveFrom(resources, call);
        assertNotEquals(resources, filtered);
        assertTrue(filtered.stream().noneMatch(resource -> resource.equals(OntologyModelConstants.getXsdDate())));
        assertTrue(filtered.stream().allMatch(resource -> resource.equals(OntologyModelConstants.getXsdStringInstance())));

        verify(query).filter(resources);
    }
}
