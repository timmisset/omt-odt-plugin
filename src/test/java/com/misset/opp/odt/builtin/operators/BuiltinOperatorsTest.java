package com.misset.opp.odt.builtin.operators;

import com.misset.opp.resolvable.Callable;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class BuiltinOperatorsTest {

    @Test
    void getStaticOperators() {
        Collection<Callable> operatorsWithoutInput = BuiltinOperators.getOperatorsWithoutInput();
        assertTrue(operatorsWithoutInput.stream().noneMatch(Callable::requiresInput));
    }

    @Test
    void getNonStaticOperators() {
        Collection<Callable> operatorsWithInput = BuiltinOperators.getOperatorsWithInput();
        assertTrue(operatorsWithInput.stream().allMatch(Callable::requiresInput));
    }

    @Test
    void getOperators() {
        assertFalse(BuiltinOperators.getOperators().isEmpty());
    }

    @Test
    void get() {
        assertEquals(LogOperator.INSTANCE, BuiltinOperators.get("LOG"));
    }
}
