package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class SplitOperator extends BuiltInStringOperator {
    private SplitOperator() {
    }

    public static final SplitOperator INSTANCE = new SplitOperator();

    @Override
    public String getName() {
        return "SPLIT";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.resolveCallInput(), holder, call);
        validateStringArgument(0, call, holder);
    }


    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE);
        }
        return null;
    }


    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE);
    }
}