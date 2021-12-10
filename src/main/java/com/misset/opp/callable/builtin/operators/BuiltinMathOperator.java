package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.util.TriConsumer;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public abstract class BuiltinMathOperator extends BuiltInOperator {

    protected static final Set<OntResource> validInputs =
            Set.of(OppModel.INSTANCE.XSD_NUMBER_INSTANCE,
                    OppModel.INSTANCE.XSD_DATE_INSTANCE,
                    OppModel.INSTANCE.XSD_DATETIME_INSTANCE,
                    OppModel.INSTANCE.XSD_DURATION_INSTANCE);
    protected static final TriConsumer<Integer, PsiCall, ProblemsHolder> validator = (i, call, holder) -> {
        Set<OntResource> ontResources = call.resolveSignatureArgument(i);
        TTLValidationUtil.validateRequiredTypes(validInputs, ontResources, holder, call.getCallSignatureArgumentElement(i));
    };

    @Override
    protected Set<OntResource> resolveFrom(Set<OntResource> resources,
                                           Call call) {
        final int signatureArguments = call.numberOfArguments();
        Set<OntResource> values = new HashSet<>();
        if (signatureArguments == 1) {
            // input = value, argument = subtraction
            values.addAll(resources);
            values.addAll(call.resolveSignatureArgument(0));
        } else if (signatureArguments > 1) {
            values.addAll(call.resolveSignatureArgument(0));
            values.addAll(call.resolveSignatureArgument(1));
        }
        if (values.stream().allMatch(OppModel.INSTANCE.XSD_INTEGER_INSTANCE::equals)) {
            return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
        } else {
            return Set.of(OppModel.INSTANCE.XSD_DECIMAL_INSTANCE);
        }
    }

}
