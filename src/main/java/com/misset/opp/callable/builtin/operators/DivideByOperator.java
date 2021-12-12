package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class DivideByOperator extends BuiltInOperator {
    private DivideByOperator() {
    }

    public static final DivideByOperator INSTANCE = new DivideByOperator();

    @Override
    public String getName() {
        return "DIVIDE_BY";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_DECIMAL_INSTANCE;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateNumber(call.resolveCallInput(), holder, call);
        validateNumberArgument(0, call, holder);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModel.INSTANCE.XSD_NUMBER_INSTANCE);
        }
        return null;
    }
}
