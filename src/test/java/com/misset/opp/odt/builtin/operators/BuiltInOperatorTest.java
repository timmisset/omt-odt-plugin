package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class BuiltInOperatorTest extends BuiltInTest {

    @Test
    void testType() {
        Assertions.assertEquals("Builtin Operator", LogOperator.INSTANCE.getType());
    }

    @Test
    void testCanBeAppliedTo() {
        Assertions.assertTrue(LogOperator.INSTANCE.canBeAppliedTo(Set.of(oppModel.XSD_STRING_INSTANCE)));
    }

    @Test
    void testIsCommand() {
        Assertions.assertFalse(LogOperator.INSTANCE.isCommand());
    }

    @Test
    void testIsStatic() {
        Assertions.assertFalse(LogOperator.INSTANCE.isStatic());
    }

    @Test
    void testIsVoid() {
        Assertions.assertFalse(LogOperator.INSTANCE.isVoid());
    }

    @Test
    void testGetCallId() {
        Assertions.assertEquals("LOG", LogOperator.INSTANCE.getCallId());
    }

    @Override
    protected void testResolve() {
        // not necessary for abstract class
    }
}
