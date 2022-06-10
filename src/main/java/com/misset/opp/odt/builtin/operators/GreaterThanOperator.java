package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class GreaterThanOperator extends BuiltInBooleanOperator {
    private GreaterThanOperator() { }
    public static final GreaterThanOperator INSTANCE = new GreaterThanOperator();

    @Override
    public String getName() {
        return "GREATER_THAN";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        Set<OntResource> resources = validateLeftRightCompatible(call, holder);
        Set<OntClass> acceptableTypes = Set.of(OppModel.INSTANCE.XSD_STRING, OppModel.INSTANCE.XSD_NUMBER);
        TTLValidationUtil.validateHasOntClass(resources, holder, call, acceptableTypes);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE, OppModel.INSTANCE.XSD_NUMBER_INSTANCE);
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE, OppModel.INSTANCE.XSD_NUMBER_INSTANCE);
    }
}
