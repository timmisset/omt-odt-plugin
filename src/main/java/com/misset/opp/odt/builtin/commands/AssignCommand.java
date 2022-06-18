package com.misset.opp.odt.builtin.commands;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AssignCommand extends BuiltInCommand {

    protected static final String EXPECTS_AN_UNEVEN_NUMBER_OF_ARGUMENTS = "Expects an uneven number of arguments";
    private static final List<String> PARAMETER_NAMES = List.of("subject", "property", "value");
    private static final HashMap<Integer, String> PARAMETERS_NAMES_MAP = new HashMap<>();

    static {
        PARAMETERS_NAMES_MAP.put(0, PARAMETER_NAMES.get(0));
        PARAMETERS_NAMES_MAP.put(1, "..." + PARAMETER_NAMES.get(1));
        PARAMETERS_NAMES_MAP.put(2, "..." + PARAMETER_NAMES.get(2));
    }

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

    @Override
    protected List<String> getParameters() {
        return PARAMETER_NAMES;
    }

    @Override
    public Map<Integer, String> getParameterNames() {
        // The assign command should return a custom names map because both the property and value should
        // be shown as a rest parameter. @ASSIGN(subject, ...property, ...value);
        return PARAMETERS_NAMES_MAP;
    }
}
