package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.ContextFactory;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class RoundOperatorTest extends BaseBuiltinTest {

    @Test
    void testName() {
        Assertions.assertEquals("ROUND", RoundOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(0, RoundOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(1, RoundOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testValidArguments() {
        assertValidInput(RoundOperator.INSTANCE, OppModelConstants.getXsdNumberInstance());
        assertValidArgument(RoundOperator.INSTANCE, 0, OppModelConstants.getXsdNumberInstance());
    }

    @Test
    void testGetAcceptableArgumentType() {
        assertGetAcceptableArgumentType(RoundOperator.INSTANCE, 0, OppModelConstants.getXsdIntegerInstance());
        assertGetAcceptableArgumentTypeIsNull(RoundOperator.INSTANCE, 1);
    }

    @Test
    void testGetAcceptableInputType() {
        assertGetAcceptableInputType(RoundOperator.INSTANCE, OppModelConstants.getXsdDecimalInstance());
    }

    @Override
    @Test
    protected void testResolve() {
        // default rounds to 0 decimals, so returns type integer
        assertResolved(RoundOperator.INSTANCE,
                Set.of(OppModelConstants.getXsdIntegerInstance()),
                Set.of(OppModelConstants.getXsdIntegerInstance()));
    }

    @Test
    void testResolveWithDecimal0Value() {
        final PsiCall call = getCall(Set.of(OppModelConstants.getXsdIntegerInstance()));
        doReturn("0").when(call).getSignatureValue(0);
        final Set<OntResource> resolve = RoundOperator.INSTANCE.resolve(ContextFactory.fromCall(call));
        Assertions.assertTrue(resolve.stream().allMatch(OppModelConstants.getXsdIntegerInstance()::equals));
    }

    @Test
    void testResolveWithDecimal1Value() {
        final PsiCall call = getCall(Collections.emptySet());
        doReturn("1").when(call).getSignatureValue(0);
        final Set<OntResource> resolve = RoundOperator.INSTANCE.resolve(ContextFactory.fromCall(call));
        Assertions.assertTrue(resolve.stream().allMatch(OppModelConstants.getXsdDecimalInstance()::equals));
    }

    @Test
    void testSpecificValidationHasWarningWhenRoundingTo0Decimals() {
        final PsiCall call = getCall(Set.of(OppModelConstants.getXsdIntegerInstance()));
        doReturn("0").when(call).getSignatureValue(0);
        RoundOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(call.getCallSignatureArgumentElement(0),
                RoundOperator.UNNECESSARY_DECIMAL_PLACES_VALUE, ProblemHighlightType.WEAK_WARNING);
    }

    @Test
    void testSpecificValidationHasWarningWhenInputIsAlreadyAllIntegers() {
        final PsiCall call = getCall(Set.of(OppModelConstants.getXsdIntegerInstance()));
        doReturn("0").when(call).getSignatureValue(0);
        doReturn(Set.of(OppModelConstants.getXsdIntegerInstance())).when(call).resolvePreviousStep();
        RoundOperator.INSTANCE.validate(call, holder);

        verify(holder).registerProblem(call,
                RoundOperator.INPUT_IS_ALREADY_AN_INTEGER, ProblemHighlightType.WEAK_WARNING);
    }

}
