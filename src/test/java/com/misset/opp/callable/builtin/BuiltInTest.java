package com.misset.opp.callable.builtin;

import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.resolvable.call.ODTCall;
import com.misset.opp.testCase.OntologyTestCase;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public abstract class BuiltInTest extends OntologyTestCase {

    protected OppModel oppModel;

    @BeforeEach
    @Override
    protected void setUp() {
        setOntologyModel();
        oppModel = OppModel.INSTANCE;
    }

    @AfterEach
    @Override
    protected void tearDown() {

    }

    @SafeVarargs
    protected final ODTCall getCall(Set<OntResource>... arguments) {
        final ODTCall call = mock(ODTCall.class);

        final List<ODTSignatureArgument> signatureArguments = Arrays.stream(arguments)
                .map(this::getSignatureArguments)
                .collect(Collectors.toList());
        doReturn(signatureArguments).when(call).getSignatureArguments();

        // mock the argument resolving
        doAnswer(invocation -> getArgumentsAtIndex(arguments, invocation.getArgument(0)))
                .when(call).resolveSignatureArgument(anyInt());

        return call;
    }
    private Set<OntResource> getArgumentsAtIndex(Set<OntResource>[] resources, int index) {
        //
        if(resources.length <= index) { return Collections.emptySet(); }
        return resources[index];
    }

    protected final ODTCall getCall(ODTSignatureArgument... arguments) {
        final ODTCall call = mock(ODTCall.class);

        doReturn(List.of(arguments)).when(call).getSignatureArguments();
        // mock the argument resolving
        doAnswer(invocation -> arguments[(int) invocation.getArgument(0)])
                .when(call).getSignatureArgument(anyInt());
        return call;
    }

    private ODTSignatureArgument getSignatureArguments(Set<OntResource> resources) {
        final ODTSignatureArgument argument = mock(ODTSignatureArgument.class);
        final ODTResolvableValue resolvableValue = mock(ODTResolvableValue.class);
        doReturn(resources).when(resolvableValue).resolve();
        doReturn(resolvableValue).when(argument).getResolvableValue();
        doReturn(resources).when(argument).resolve();
        return argument;
    }

    @SafeVarargs
    protected final void assertResolved(Builtin builtin,
                                        OntResource expectedResource,
                                        Set<OntResource>... callArguments) {
        assertResolved(builtin, Collections.emptySet(), Set.of(expectedResource), callArguments);
    }

    @SafeVarargs
    protected final void assertResolved(Builtin builtin,
                                        OntResource inputResource,
                                        OntResource expectedResource,
                                        Set<OntResource>... callArguments) {
        assertResolved(builtin, Set.of(inputResource), Set.of(expectedResource), callArguments);
    }

    @SafeVarargs
    protected final void assertResolved(Builtin builtin,
                                        Set<OntResource> inputResources,
                                        Set<OntResource> expectedResources,
                                        Set<OntResource>... callArguments) {
        final ODTCall call = getCall(callArguments);
        final Set<OntResource> resources = builtin.resolve(inputResources, call);
        assertEquals(expectedResources.size(), resources.size());
        assertTrue(resources.containsAll(expectedResources));
    }

    protected final void assertCombinesInput(Builtin builtin) {
        assertResolved(builtin,
                Collections.emptySet(),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE,
                        oppModel.XSD_STRING_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE));
    }

    protected final void assertReturnsFirstArgument(Builtin builtin) {
        assertResolved(builtin,
                Collections.emptySet(),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE));
    }

    /**
     * Checks if the call returns a specific type regardless of the call arguments or input
     */
    protected final void assertReturns(Builtin builtin, OntResource resource) {
        assertResolved(builtin,
                Collections.emptySet(),
                Set.of(resource),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE));
    }

    protected final void assertReturnsVoid(Builtin builtin) {
        assertResolved(builtin,
                Collections.emptySet(),
                Collections.emptySet(),
                Set.of(oppModel.XSD_BOOLEAN_INSTANCE),
                Set.of(oppModel.XSD_STRING_INSTANCE));
    }

    protected abstract void testResolve();

}
