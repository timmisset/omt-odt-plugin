package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

public abstract class BuiltInBooleanOperator extends BuiltInOperator {

    protected static final String ERROR = "Requires previous step to be a boolean when there is only 1 argument";

    protected void validateSingleArgumentInputBoolean(PsiCall call, ProblemsHolder holder) {
        if (call.getNumberOfArguments() == 1) {
            boolean b = TTLValidationUtil.validateBoolean(call.resolvePreviousStep(), holder, call);
            if (!b) {
                holder.registerProblem(call, ERROR, ProblemHighlightType.ERROR);
            }
        }
    }

    @Override
    public OntResource resolveSingle() {
        return OppModel.INSTANCE.XSD_BOOLEAN_INSTANCE;
    }

    @Override
    protected boolean hasFixedReturnType() {
        return true;
    }
}
