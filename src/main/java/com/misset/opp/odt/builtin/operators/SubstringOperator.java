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

public class SubstringOperator extends BuiltInStringOperator {
    private SubstringOperator() {
    }

    public static final SubstringOperator INSTANCE = new SubstringOperator();
    private static final List<String> PARAMETER_NAMES = List.of("begin", "end");

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
        OntologyValidationUtil.validateString(call.resolvePreviousStep(), holder, call);
        ArgumentValidator.validateAllArguments(call, holder, ArgumentValidator::validateIntegerArgument);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        return Set.of(OntologyModelConstants.getXsdIntegerInstance());
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
