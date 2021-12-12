package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class JoinOperator extends BuiltInStringOperator {
    private JoinOperator() { }
    public static final JoinOperator INSTANCE = new JoinOperator();

    @Override
    public String getName() {
        return "JOIN";
    }

    @Override
    public int minNumberOfArguments() {
        return 0;
    }

    @Override
    public int maxNumberOfArguments() {
        return 1;
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
}
