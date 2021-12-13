package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class MinOperator extends BuiltInCollectionOperator {
    private MinOperator() { }
    public static final MinOperator INSTANCE = new MinOperator();

    @Override
    public String getName() {
        return "MIN";
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
        TTLValidationUtil.validateHasOntClass(call.resolveCallInput(),
                holder,
                call,
                acceptableTypes);
        TTLValidationUtil.validateHasOntClass(call.resolveSignatureArgument(0),
                holder,
                call.getCallSignatureArgumentElement(0),
                acceptableTypes);
    }


    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModel.INSTANCE.XSD_NUMBER_INSTANCE, OppModel.INSTANCE.XSD_DATETIME_INSTANCE);
        }
        return null;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.XSD_NUMBER_INSTANCE, OppModel.INSTANCE.XSD_DATETIME_INSTANCE);
    }
}
