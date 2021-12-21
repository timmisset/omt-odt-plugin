package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.List;
import java.util.Set;

public class ContainsOperator extends BuiltInBooleanOperator {
    private ContainsOperator() { }
    public static final ContainsOperator INSTANCE = new ContainsOperator();

    @Override
    public String getName() {
        return "CONTAINS";
    }

    @Override
    public int maxNumberOfArguments() {
        return 2;
    }

    @Override
    public List<String> getFlags() {
        return IGNORE_CASE_FLAG;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateString(call.resolveCallInput(), holder, call);
        validateStringArgument(0, call, holder);
        validateBooleanArgument(1, call, holder);
        validateIgnoreCaseFlagUsage(1, call, holder);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE);
        } else if (index == 1) {
            return Set.of(OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE);
        }
        return null;
    }


    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.XSD_STRING_INSTANCE);
    }
}
