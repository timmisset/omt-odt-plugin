package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public class SkipOperator extends BuiltInCollectionOperator {
    private SkipOperator() { }

    public static final SkipOperator INSTANCE = new SkipOperator();
    private static final List<String> PARAMETER_NAMES = List.of("amount");

    @Override
    public String getName() {
        return "SKIP";
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        ArgumentValidator.validateIntegerArgument(0, call, holder);
    }

    @Override
    public @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OntologyModelConstants.getXsdIntegerInstance());
        }
        return null;
    }

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }
}
