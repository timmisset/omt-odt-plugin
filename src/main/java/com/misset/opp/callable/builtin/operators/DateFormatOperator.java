package com.misset.opp.callable.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.callable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class DateFormatOperator extends BuiltInStringOperator {
    private DateFormatOperator() { }
    public static final DateFormatOperator INSTANCE = new DateFormatOperator();

    @Override
    public String getName() {
        // that's correct, the classname and function call in this ODT operator are different
        return "FORMAT_DATE";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateDateTime(call.resolveCallInput(), holder, call);
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
        return Set.of(OppModel.INSTANCE.XSD_DATETIME_INSTANCE);
    }
}
