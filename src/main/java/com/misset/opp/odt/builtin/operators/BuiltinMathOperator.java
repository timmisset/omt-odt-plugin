package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.util.TriConsumer;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public abstract class BuiltinMathOperator extends AbstractBuiltInOperator {

    protected static final Set<OntResource> validInputs =
            Set.of(OppModelConstants.XSD_NUMBER_INSTANCE,
                    OppModelConstants.XSD_DATE_INSTANCE,
                    OppModelConstants.XSD_DATETIME_INSTANCE,
                    OppModelConstants.XSD_DURATION_INSTANCE);
    protected static final TriConsumer<Integer, PsiCall, ProblemsHolder> validator = (i, call, holder) -> {
        Set<OntResource> ontResources = call.resolveSignatureArgument(i);
        TTLValidationUtil.validateRequiredTypes(validInputs, ontResources, holder, call.getCallSignatureArgumentElement(i));
    };

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           PsiCall call) {
        final int signatureArguments = call.getNumberOfArguments();
        Set<OntResource> values = new HashSet<>();
        if (signatureArguments == 1) {
            // input = value, argument = subtraction
            values.addAll(resources);
            values.addAll(call.resolveSignatureArgument(0));
        } else if (signatureArguments > 1) {
            values.addAll(call.resolveSignatureArgument(0));
            values.addAll(call.resolveSignatureArgument(1));
        }
        if (values.stream().allMatch(OppModelConstants.XSD_INTEGER_INSTANCE::equals)) {
            return Set.of(OppModelConstants.XSD_INTEGER_INSTANCE);
        } else {
            return Set.of(OppModelConstants.XSD_DECIMAL_INSTANCE);
        }
    }

}
