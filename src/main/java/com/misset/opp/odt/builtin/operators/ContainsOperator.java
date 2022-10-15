package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class ContainsOperator extends BuiltInBooleanOperator {
    private ContainsOperator() {
    }

    public static final ContainsOperator INSTANCE = new ContainsOperator();
    private static final List<String> PARAMETER_NAMES = List.of("value");

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
        OntologyValidationUtil.getInstance(call.getProject()).validateString(call.resolvePreviousStep(), holder, call);
        ArgumentValidator.validateStringArgument(0, call, holder);
        ArgumentValidator.validateBooleanArgument(1, call, holder);
        validateIgnoreCaseFlagUsage(1, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OntologyModelConstants.getXsdStringInstance());
        } else if (index == 1) {
            return Set.of(OntologyModelConstants.getXsdBooleanInstance());
        }
        return null;
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OntologyModelConstants.getXsdStringInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
