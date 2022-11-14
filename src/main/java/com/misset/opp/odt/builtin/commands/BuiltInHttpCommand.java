package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class BuiltInHttpCommand extends AbstractBuiltInCommand {

    @Override
    public OntResource resolveSingle() {
        return OntologyModelConstants.getJsonObject();
    }

    @Override
    public int minNumberOfArguments() {
        return 1;
    }

    @Override
    public int maxNumberOfArguments() {
        return 4;
    }

    @Override
    protected void specificValidation(PsiCall call,
                                      ProblemsHolder holder) {
        ArgumentValidator.validateStringArgument(0, call, holder);
        ArgumentValidator.validateJSONArgument(1, call, holder);
        ArgumentValidator.validateBooleanArgument(2, call, holder);
        ArgumentValidator.validateJSONArgument(3, call, holder);
    }

    @Override
    protected @Nullable Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OntologyModelConstants.getXsdStringInstance());
        } else if (index == 1) {
            return Set.of(OntologyModelConstants.getJsonObject());
        } else if (index == 2) {
            return Set.of(OntologyModelConstants.getXsdBooleanInstance());
        } else if (index == 3) {
            return Set.of(OntologyModelConstants.getJsonObject());
        }
        return null;
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        return Set.of(OntologyModelConstants.getXsdIntegerInstance());
    }
}
