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

public class TruncOperator extends BuiltInCollectionOperator {
    private TruncOperator() {
    }

    public static final TruncOperator INSTANCE = new TruncOperator();
    private static final List<String> PARAMETER_NAMES = List.of("precision");

    @Override
    public String getName() {
        return "TRUNC";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        OntologyValidationUtil.getInstance(call.getProject()).validateDateTime(call.resolvePreviousStep(), holder, call);
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
    public Set<OntResource> getAcceptableInputType() {
        return Set.of(OntologyModelConstants.getXsdDatetimeInstance());
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
