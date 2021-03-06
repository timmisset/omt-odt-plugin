package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
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
        TTLValidationUtil.validateDateTime(call.resolvePreviousStep(), holder, call);
        validateStringArgument(0, call, holder);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
        }
        return null;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModelConstants.XSD_DATETIME_INSTANCE);
    }
}
