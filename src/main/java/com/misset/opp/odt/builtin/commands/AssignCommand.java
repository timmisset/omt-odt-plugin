package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public class AssignCommand extends BuiltInCommand {

    protected static final String EXPECTS_AN_UNEVEN_NUMBER_OF_ARGUMENTS = "Expects an uneven number of arguments";

    private AssignCommand() {
    }

    public static final AssignCommand INSTANCE = new AssignCommand();

    @Override
    public String getName() {
        return "ASSIGN";
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    public int minNumberOfArguments() {
        return 3;
    }

    @Override
    protected Set<OntResource> resolveFrom(PsiCall call) {
        return call.resolveSignatureArgument(0);
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index > 1) {
            if (index % 2 == 0) {
                return call.resolveSignatureArgument(index - 1);
            }
        }
        return null;
    }

    /**
     * Validate that the AssignCommand is called with the right amount of parameters and an uneven amount
     * Any further inspection requires information much more extensive than the generic PsiCall can provide.
     */
    @Override
    public void specificValidation(PsiCall call,
                                   ProblemsHolder holder) {
        if (call.getNumberOfArguments() % 2 == 0) {
            holder.registerProblem(call, EXPECTS_AN_UNEVEN_NUMBER_OF_ARGUMENTS, ProblemHighlightType.ERROR);
        }
    }
}
