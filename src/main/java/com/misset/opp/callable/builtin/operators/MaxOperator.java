package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntClass;

import java.util.Set;

public class MaxOperator extends BuiltInCollectionOperator {
    private MaxOperator() { }
    public static final MaxOperator INSTANCE = new MaxOperator();

    @Override
    public String getName() {
        return "MAX";
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntClass> acceptableTypes = Set.of(OppModel.INSTANCE.XSD_NUMBER, OppModel.INSTANCE.XSD_DATETIME);
        TTLValidationUtil.validateHasOntClass(call.getCallInputType(),
                holder,
                call,
                acceptableTypes);
        TTLValidationUtil.validateHasOntClass(call.resolveSignatureArgument(0),
                holder,
                call.getCallSignatureArgumentElement(0),
                acceptableTypes);
    }
}
