package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.BaseBuiltinTest;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

class AssignCommandTest extends BaseBuiltinTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(AssignCommand.INSTANCE);
    }

    @Test
    void testAcceptableArgumentType() {
        PsiCall call = getCall(Set.of(OppModelConstants.getXsdStringInstance()), Set.of(OppModelConstants.getXsdBooleanInstance()));

        Assertions.assertTrue(AssignCommand.INSTANCE.getAcceptableArgumentType(0, call)
                .contains(OppModelConstants.getOwlThingInstance()));
        Assertions.assertTrue(AssignCommand.INSTANCE.getAcceptableArgumentType(1, call)
                .contains(OppModelConstants.getOwlThingInstance()));
        Assertions.assertTrue(AssignCommand.INSTANCE.getAcceptableArgumentType(2, call)
                .contains(OppModelConstants.getXsdBooleanInstance()));
        Assertions.assertFalse(AssignCommand.INSTANCE.getAcceptableArgumentType(2, call)
                .contains(OppModelConstants.getXsdStringInstance()));
    }

    @Test
    void testSpecificValidationHasErrorWhenCalledWithEvenNumberOfParameters() {
        PsiCall call = mock(PsiCall.class);
        ProblemsHolder holder = mock(ProblemsHolder.class);
        doReturn(2).when(call).getNumberOfArguments();

        AssignCommand.INSTANCE.specificValidation(call, holder);
        verify(holder, times(1)).registerProblem(call, AssignCommand.EXPECTS_AN_UNEVEN_NUMBER_OF_ARGUMENTS, ProblemHighlightType.ERROR);
    }

    @Test
    void testSpecificValidationNoHasErrorWhenCalledWithUnEvenNumberOfParameters() {
        PsiCall call = mock(PsiCall.class);
        ProblemsHolder holder = mock(ProblemsHolder.class);
        doReturn(3).when(call).getNumberOfArguments();

        AssignCommand.INSTANCE.specificValidation(call, holder);
        verify(holder, never()).registerProblem(call, AssignCommand.EXPECTS_AN_UNEVEN_NUMBER_OF_ARGUMENTS, ProblemHighlightType.ERROR);
    }

    @Test
    void testReturnsTrueForIsStatic() {
        Assertions.assertTrue(AssignCommand.INSTANCE.isStatic());
    }

    @Test
    void testNumberOfArguments() {
        Assertions.assertEquals(3, AssignCommand.INSTANCE.minNumberOfArguments());
        Assertions.assertEquals(-1, AssignCommand.INSTANCE.maxNumberOfArguments());
    }

    @Test
    void testName() {
        Assertions.assertEquals("ASSIGN", AssignCommand.INSTANCE.getName());
    }

    @Test
    void testIsNotVoid() {
        Assertions.assertFalse(AssignCommand.INSTANCE.isVoid());
    }
}
