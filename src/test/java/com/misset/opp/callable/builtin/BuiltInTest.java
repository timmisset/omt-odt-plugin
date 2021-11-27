package com.misset.opp.callable.builtin;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public abstract class BuiltInTest extends OMTOntologyTestCase {

    protected OppModel oppModel;
    protected ProblemsHolder holder = mock(ProblemsHolder.class);
    protected PsiElement signature = mock(PsiElement.class);
    protected PsiElement firstArgument = mock(PsiElement.class);
    protected PsiElement secondArgument = mock(PsiElement.class);
    protected PsiElement thirdArgument = mock(PsiElement.class);
    protected PsiElement fourthArgument = mock(PsiElement.class);
    private PsiElement[] mockArguments = new PsiElement[]{firstArgument, secondArgument, thirdArgument, fourthArgument};

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
    protected final PsiCall getCall(Set<OntResource>... arguments) {
        final PsiCall call = mock(PsiCall.class);

        doReturn(List.of(arguments)).when(call).resolveSignatureArguments();
        doReturn(arguments.length).when(call).numberOfArguments();
        doReturn(signature).when(call).getCallSignatureElement();
        for (int i = 0; i < mockArguments.length; i++) {
            doReturn(mockArguments[i]).when(call).getCallSignatureArgumentElement(i);
        }

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
        final Call call = getCall(callArguments);
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

    protected void testArgument(Builtin builtin,
                                int index,
                                OntResource expected,
                                String errorMessage) {
        OntResource invalidArgument = expected.equals(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE) ? OppModel.INSTANCE.XSD_STRING_INSTANCE : OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE;
        testArgument(builtin, index, expected, errorMessage, invalidArgument);
    }

    protected void testArgument(Builtin builtin,
                                int index,
                                OntResource expected,
                                String errorMessage,
                                OntResource invalidArgument) {
        final PsiElement callArgument = mockArguments[index];

        List<Set<OntResource>> validArguments = new ArrayList<>();
        List<Set<OntResource>> invalidArguments = new ArrayList<>();
        while (validArguments.size() <= index) {
            validArguments.add(Set.of(expected));
            invalidArguments.add(Set.of(invalidArgument));
        }

        final PsiCall validCall = getCall(validArguments.toArray(Set[]::new));
        final ProblemsHolder validHolder = mock(ProblemsHolder.class);
        builtin.validate(validCall, validHolder);
        verify(validHolder, never()).registerProblem(callArgument, errorMessage, ProblemHighlightType.ERROR);

        final ProblemsHolder invalidHolder = mock(ProblemsHolder.class);
        final PsiCall invalidCall = getCall(invalidArguments.toArray(Set[]::new));
        builtin.validate(invalidCall, invalidHolder);
        verify(invalidHolder).registerProblem(callArgument, errorMessage, ProblemHighlightType.ERROR);
    }
}
