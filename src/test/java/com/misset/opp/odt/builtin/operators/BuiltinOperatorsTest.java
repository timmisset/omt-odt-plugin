package com.misset.opp.odt.builtin.operators;

import com.misset.opp.resolvable.Callable;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class BuiltinOperatorsTest {

    @Test
    void getStaticOperators() {
        Collection<Callable> staticOperators = BuiltinOperators.getStaticOperators();
        assertTrue(staticOperators.stream().allMatch(Callable::isStatic));
    }

    @Test
    void getNonStaticOperators() {
        Collection<Callable> staticOperators = BuiltinOperators.getNonStaticOperators();
        assertTrue(staticOperators.stream().noneMatch(Callable::isStatic));
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
