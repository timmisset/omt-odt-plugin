package com.misset.opp.odt.builtin;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.ContextFactory;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public abstract class BaseBuiltinTest {

    protected OppModel oppModel;
    protected ProblemsHolder holder = mock(ProblemsHolder.class);
    protected PsiElement signature = mock(PsiElement.class);
    protected PsiElement firstArgument = mock(PsiElement.class);
    protected PsiElement secondArgument = mock(PsiElement.class);
    protected PsiElement thirdArgument = mock(PsiElement.class);
    protected PsiElement fourthArgument = mock(PsiElement.class);
    private final PsiElement[] mockArguments = new PsiElement[]{firstArgument, secondArgument, thirdArgument, fourthArgument};

    @BeforeEach
    protected void setUp() {
        oppModel = OMTOntologyTestCase.initOntologyModel();
    }

    @AfterEach
    protected void tearDown() {

    }

    @SafeVarargs
    protected final PsiCall getCall(Set<OntResource>... arguments) {
        final PsiCall call = mock(PsiCall.class);

        doReturn(List.of(arguments)).when(call).resolveSignatureArguments();
        doReturn(arguments.length).when(call).getNumberOfArguments();
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
        if (resources.length <= index) {
            return Collections.emptySet();
        }
        return resources[index];
    }

    @SafeVarargs
    protected final void assertResolved(AbstractBuiltin builtin,
                                        OntResource expectedResource,
                                        Set<OntResource>... callArguments) {
        assertResolved(builtin, Collections.emptySet(), Set.of(expectedResource), callArguments);
    }

    @SafeVarargs
    protected final void assertResolved(AbstractBuiltin builtin,
                                        OntResource inputResource,
                                        OntResource expectedResource,
                                        Set<OntResource>... callArguments) {
        assertResolved(builtin, Set.of(inputResource), Set.of(expectedResource), callArguments);
    }

    @SafeVarargs
    protected final void assertResolved(AbstractBuiltin builtin,
                                        Set<OntResource> inputResources,
                                        Set<OntResource> expectedResources,
                                        Set<OntResource>... callArguments) {
        final PsiCall call = getCall(callArguments);
        doReturn(inputResources).when(call).resolvePreviousStep();
        Context context = ContextFactory.fromCall(call);
        final Set<OntResource> resources = builtin.resolve(context);
        assertEquals(expectedResources.size(), resources.size());
        assertTrue(resources.containsAll(expectedResources));
    }

    protected final void assertCombinesInput(AbstractBuiltin builtin) {
        assertResolved(builtin,
                Collections.emptySet(),
                Set.of(OppModelConstants.getXsdBooleanInstance(),
                        OppModelConstants.getXsdStringInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdStringInstance()));
    }

    protected final void assertReturnsFirstArgument(AbstractBuiltin builtin) {
        assertResolved(builtin,
                Collections.emptySet(),
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdStringInstance()));
    }

    /**
     * Checks if the call returns a specific type regardless of the call arguments or input
     */
    protected final void assertReturns(AbstractBuiltin builtin, OntResource resource) {
        assertResolved(builtin,
                Collections.emptySet(),
                Set.of(resource),
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdStringInstance()));
    }

    protected final void assertReturnsVoid(AbstractBuiltin builtin) {
        assertResolved(builtin,
                Collections.emptySet(),
                Collections.emptySet(),
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdStringInstance()));
    }

    protected abstract void testResolve();

    protected void testArgument(AbstractBuiltin builtin,
                                int index,
                                OntResource expected,
                                String errorMessage) {
        OntResource invalidArgument = expected.equals(OppModelConstants.getXsdBooleanInstance()) ? OppModelConstants.getXsdStringInstance() : OppModelConstants.getXsdBooleanInstance();
        testArgument(builtin, index, expected, errorMessage, invalidArgument);
    }

    protected void assertValidInput(AbstractBuiltin builtin,
                                    OntResource input) {
        PsiCall call = getCall();
        doReturn(Set.of(input)).when(call).resolvePreviousStep();
        builtin.validate(call, holder);
        verify(holder, never()).registerProblem(eq(call), anyString(), any(ProblemHighlightType.class));
    }

    protected void assertInvalidInput(AbstractBuiltin builtin,
                                      OntResource input,
                                      String errorMessage) {
        PsiCall call = getCall();
        doReturn(Set.of(input)).when(call).resolvePreviousStep();
        builtin.validate(call, holder);
        verify(holder).registerProblem(eq(call), startsWith(errorMessage), any(ProblemHighlightType.class));
    }

    protected void testArgument(AbstractBuiltin builtin,
                                int index,
                                OntResource expected,
                                String errorMessage,
                                OntResource invalidArgument) {
        assertValidArgument(builtin, index, expected);
        assertInvalidArgument(builtin, index, invalidArgument, errorMessage);
    }

    private void assertArgument(AbstractBuiltin builtin,
                                int index,
                                OntResource argumentType) {
        reset(holder);
        List<Set<OntResource>> invalidArguments = new ArrayList<>();
        while (invalidArguments.size() <= index) {
            invalidArguments.add(Set.of(argumentType));
        }

        final PsiCall invalidCall = getCall(invalidArguments.toArray(Set[]::new));
        builtin.validate(invalidCall, holder);
    }

    protected void assertValidArgument(AbstractBuiltin builtin,
                                       int index,
                                       OntResource argumentType) {
        final PsiElement callArgument = mockArguments[index];
        assertArgument(builtin, index, argumentType);
        verify(holder, never()).registerProblem(eq(callArgument), anyString(), eq(ProblemHighlightType.ERROR));
    }

    protected void assertInvalidArgument(AbstractBuiltin builtin,
                                         int index,
                                         OntResource argumentType,
                                         String errorMessage) {
        final PsiElement callArgument = mockArguments[index];
        assertArgument(builtin, index, argumentType);
        verify(holder).registerProblem(eq(callArgument), startsWith(errorMessage), eq(ProblemHighlightType.ERROR));
    }

    protected void assertGetAcceptableArgumentTypeIsNull(AbstractBuiltin builtin, int index) {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = builtin.getAcceptableArgumentTypeWithContext(index, call);
        assertNull(acceptableArgumentTypeWithContext);
    }

    protected void assertGetAcceptableArgumentType(AbstractBuiltin builtin, int index, OntResource exceptedType) {
        assertGetAcceptableArgumentType(builtin, index, Set.of(exceptedType));
    }

    protected void assertGetAcceptableArgumentType(AbstractBuiltin builtin, int index, Set<OntResource> exceptedTypes) {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> acceptableArgumentTypeWithContext = builtin.getAcceptableArgumentTypeWithContext(index, call);

        assertNotNull(acceptableArgumentTypeWithContext);
        assertEquals(exceptedTypes.size(), acceptableArgumentTypeWithContext.size());

        assertTrue(acceptableArgumentTypeWithContext.containsAll(exceptedTypes));
    }

    protected void assertGetAcceptableArgumentTypeSameAsPreviousStep(AbstractBuiltin builtin, int index) {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> previousStep = Set.of(OppModelConstants.getXsdStringInstance());
        doReturn(previousStep).when(call).resolvePreviousStep();

        Set<OntResource> acceptableArgumentTypeWithContext = builtin.getAcceptableArgumentTypeWithContext(index, call);

        assertNotNull(acceptableArgumentTypeWithContext);
        assertEquals(previousStep.size(), acceptableArgumentTypeWithContext.size());

        assertTrue(acceptableArgumentTypeWithContext.containsAll(previousStep));
    }

    protected void assertGetAcceptableArgumentTypeSameAsArgument(AbstractBuiltin builtin, int index, int sameAsIndex) {
        PsiCall call = mock(PsiCall.class);
        Set<OntResource> atIndex = Set.of(OppModelConstants.getXsdStringInstance());
        doReturn(atIndex).when(call).resolveSignatureArgument(sameAsIndex);

        Set<OntResource> acceptableArgumentTypeWithContext = builtin.getAcceptableArgumentTypeWithContext(index, call);

        assertNotNull(acceptableArgumentTypeWithContext);
        assertEquals(atIndex.size(), acceptableArgumentTypeWithContext.size());

        assertTrue(acceptableArgumentTypeWithContext.containsAll(atIndex));
    }

    protected void assertGetAcceptableInputType(AbstractBuiltin builtin, OntResource resource) {
        assertGetAcceptableInputType(builtin, Set.of(resource));
    }

    protected void assertGetAcceptableInputType(AbstractBuiltin builtin, Set<OntResource> resources) {
        Set<OntResource> acceptableInputType = builtin.getAcceptableInputType();
        assertTrue(acceptableInputType.containsAll(resources));
    }
}
