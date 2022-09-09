package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class IIFOperatorTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(IIfOperator.INSTANCE,
                Collections.emptySet(),
                Set.of(OppModelConstants.getXsdBooleanInstance(), OppModelConstants.getXsdStringInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdStringInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()));
    }

    @Test
    protected void testResolveSingleArgument() {
        assertResolved(IIfOperator.INSTANCE,
                Collections.emptySet(),
                Set.of(OppModelConstants.getXsdStringInstance()),
                Set.of(OppModelConstants.getXsdBooleanInstance()),
                Set.of(OppModelConstants.getXsdStringInstance()));
    }

    @Test
    void testName() {
        Assertions.assertEquals("IIF", IIfOperator.INSTANCE.getName());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(2, IIfOperator.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(3, IIfOperator.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testGetAcceptableArgumentTypes() {
        assertGetAcceptableArgumentType(IIfOperator.INSTANCE, 0, OppModelConstants.getXsdBooleanInstance());
        assertGetAcceptableArgumentTypeSameAsArgument(IIfOperator.INSTANCE, 1, 2);
        assertGetAcceptableArgumentTypeSameAsArgument(IIfOperator.INSTANCE, 2, 1);
        assertGetAcceptableArgumentTypeIsNull(IIfOperator.INSTANCE, 3);
    }

    @Test
    void testValidArguments() {
        assertValidArgument(IIfOperator.INSTANCE, 0, OppModelConstants.getXsdBooleanInstance());
        assertInvalidArgument(IIfOperator.INSTANCE, 0, OppModelConstants.getXsdStringInstance(), TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testSpecificValidationHasWarningWhenIncompatibleTypes() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdBooleanInstance()), Set.of(OppModelConstants.getXsdBooleanInstance()), Set.of(OppModelConstants.getXsdStringInstance()));
        IIfOperator.INSTANCE.validate(call, holder);
        verify(holder)
                .registerProblem(call, "Possible outcomes are incompatible, not illegal but it smells fishy", ProblemHighlightType.WEAK_WARNING);
    }

    @Test
    void testSpecificValidationHasNoWarningWhenNoIncompatibleTypes() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdBooleanInstance()), Set.of(OppModelConstants.getXsdStringInstance()), Set.of(OppModelConstants.getXsdStringInstance()));
        IIfOperator.INSTANCE.validate(call, holder);
        verify(holder, never())
                .registerProblem(call, "Possible outcomes are incompatible, not illegal but it smells fishy", ProblemHighlightType.WEAK_WARNING);
    }

    @Test
    void testRequiresInput() {
        Assertions.assertFalse(IIfOperator.INSTANCE.requiresInput());
    }
}
