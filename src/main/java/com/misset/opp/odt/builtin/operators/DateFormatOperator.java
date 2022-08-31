package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class DateFormatOperator extends BuiltInStringOperator {
    private DateFormatOperator() { }

    public static final DateFormatOperator INSTANCE = new DateFormatOperator();
    private static final List<String> PARAMETER_NAMES = List.of("format");
    @Override
    public String getName() {
        // that's correct, the classname and function call in this ODT operator are different
        return "FORMAT_DATE";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        TTLValidationUtil.validateDateTime(call.resolvePreviousStep(), holder, call);
        ArgumentValidator.validateStringArgument(0, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.getXsdStringInstance());
        }
        return null;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModelConstants.getXsdDatetimeInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
