package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class SubstringOperator extends BuiltInStringOperator {
    private SubstringOperator() { }
    public static final SubstringOperator INSTANCE = new SubstringOperator();

    @Override
    public String getName() {
        return "SUBSTRING";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.resolveCallInput(), holder, call);
        validateAllArguments(call, holder, this::validateIntegerArgument);
    }


    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OppModel.INSTANCE.XSD_INTEGER_INSTANCE);
    }
}
