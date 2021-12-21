package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class PlusOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        // all integers resolve to integer
        assertResolved(PlusOperator.INSTANCE,
                Set.of(oppModel.XSD_INTEGER_INSTANCE),
                Set.of(oppModel.XSD_INTEGER_INSTANCE),
                Set.of(oppModel.XSD_INTEGER_INSTANCE)
                );
    }

    @Test
    protected void testResolveDecimal() {
        // all integers resolve to integer
        assertResolved(PlusOperator.INSTANCE,
                Set.of(oppModel.XSD_INTEGER_INSTANCE),
                Set.of(oppModel.XSD_DECIMAL_INSTANCE),
                Set.of(oppModel.XSD_DECIMAL_INSTANCE)
        );
    }

    @Test
    protected void testResolveDecimalLeading() {
        // all integers resolve to integer
        assertResolved(PlusOperator.INSTANCE,
                Set.of(oppModel.XSD_DECIMAL_INSTANCE),
                Set.of(oppModel.XSD_DECIMAL_INSTANCE),
                Set.of(oppModel.XSD_INTEGER_INSTANCE)
        );
    }

}
