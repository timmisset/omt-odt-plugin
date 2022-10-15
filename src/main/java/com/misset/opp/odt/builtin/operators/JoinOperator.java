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

public class JoinOperator extends BuiltInStringOperator {
    private JoinOperator() {
    }

    public static final JoinOperator INSTANCE = new JoinOperator();
    private static final List<String> PARAMETER_NAMES = List.of("seperator");

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
        OntologyValidationUtil.getInstance(call.getProject()).validateString(call.resolvePreviousStep(), holder, call);
        ArgumentValidator.validateStringArgument(0, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OntologyModelConstants.getXsdStringInstance());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
