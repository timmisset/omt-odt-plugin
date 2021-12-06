package com.misset.opp.callable.builtin.operators;

import com.misset.opp.callable.builtin.BuiltInTest;
import org.junit.jupiter.api.Test;

import java.util.Set;

class TypeOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        assertResolved(TypeOperator.INSTANCE,
                Set.of(oppModel.XSD_DATE_INSTANCE),
                Set.of(oppModel.XSD_DATE));

        assertResolved(TypeOperator.INSTANCE,
                Set.of(oppModel.MEDEWERKER_GRAPH),
                Set.of(oppModel.IRI));
    }

}
