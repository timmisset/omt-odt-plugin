package com.misset.opp.odt.builtin;

import com.misset.opp.odt.builtin.operators.LogOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BuiltinAbstractTest extends BaseBuiltinTest {

    @Test
    void testGetAcceptableArgumentTypeWithContextReturnsNull() {
        Assertions.assertNull(LogOperator.INSTANCE.getAcceptableArgumentTypeWithContext(0, null));
    }

    @Test
    void testGetParameterNamesReturnsNewMap() {
        Assertions.assertTrue(LogOperator.INSTANCE.getParameterNames().isEmpty());
    }

    @Override
    protected void testResolve() {
        // not needed for abstract class
    }
}
