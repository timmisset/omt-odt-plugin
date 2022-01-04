package com.misset.opp.odt.builtin.operators;

import com.misset.opp.odt.builtin.BuiltInTest;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.doReturn;

class RoundOperatorTest extends BuiltInTest {

    @Override
    @Test
    protected void testResolve() {
        // default rounds to 0 decimals, so returns type integer
        assertResolved(MinOperator.INSTANCE,
                Set.of(oppModel.XSD_INTEGER_INSTANCE),
                Set.of(oppModel.XSD_INTEGER_INSTANCE));
    }

    @Test
    protected void testResolveWithDecimal0Value() {
        // default rounds to 0 decimals, so returns type integer
        final PsiCall call = getCall();
        doReturn("0").when(call).getSignatureValue(0);
        final Set<OntResource> resolve = RoundOperator.INSTANCE.resolve(Context.fromCall(call));
        Assertions.assertTrue(resolve.stream().allMatch(OppModel.INSTANCE.XSD_INTEGER_INSTANCE::equals));
    }

    @Test
    protected void testResolveWithDecimal1Value() {
        // default rounds to 0 decimals, so returns type integer
        final PsiCall call = getCall(Collections.emptySet());
        doReturn("1").when(call).getSignatureValue(0);
        final Set<OntResource> resolve = RoundOperator.INSTANCE.resolve(Context.fromCall(call));
        Assertions.assertTrue(resolve.stream().allMatch(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE::equals));
    }

}
