package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.util.TriConsumer;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.HashSet;
import java.util.Set;

public abstract class BuiltinMathOperator extends AbstractBuiltInOperator {

    protected static final Set<OntResource> validInputs =
            Set.of(OntologyModelConstants.getXsdNumberInstance(),
                    OntologyModelConstants.getXsdDateInstance(),
                    OntologyModelConstants.getXsdDatetimeInstance(),
                    OntologyModelConstants.getXsdDurationInstance());
    protected static final TriConsumer<Integer, PsiCall, ProblemsHolder> validator = (i, call, holder) -> {
        Set<OntResource> ontResources = call.resolveSignatureArgument(i);
        OntologyValidationUtil.validateRequiredTypes(validInputs, ontResources, holder, call.getCallSignatureArgumentElement(i));
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
        if (values.stream().allMatch(OntologyModelConstants.getXsdIntegerInstance()::equals)) {
            return Set.of(OntologyModelConstants.getXsdIntegerInstance());
        } else {
            return Set.of(OntologyModelConstants.getXsdDecimalInstance());
        }
    }

}
