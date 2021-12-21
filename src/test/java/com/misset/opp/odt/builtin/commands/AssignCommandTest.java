package com.misset.opp.odt.builtin.commands;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.resolvable.psi.PsiCall;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class AssignCommandTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertReturnsFirstArgument(AssignCommand.INSTANCE);
    }

    @Test
    void testAcceptableArgumentType() {
        PsiCall call = getCall(Set.of(oppModel.XSD_STRING_INSTANCE), Set.of(oppModel.XSD_BOOLEAN_INSTANCE));

        Assertions.assertTrue(AssignCommand.INSTANCE.getAcceptableArgumentType(0, call)
                .contains(oppModel.OWL_THING_INSTANCE));
        Assertions.assertTrue(AssignCommand.INSTANCE.getAcceptableArgumentType(1, call)
                .contains(oppModel.OWL_THING_INSTANCE));
        Assertions.assertTrue(AssignCommand.INSTANCE.getAcceptableArgumentType(2, call)
                .contains(oppModel.XSD_BOOLEAN_INSTANCE));
        Assertions.assertFalse(AssignCommand.INSTANCE.getAcceptableArgumentType(2, call)
                .contains(oppModel.XSD_STRING_INSTANCE));
    }
}
