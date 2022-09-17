package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.model.OntologyModelConstants;
import com.misset.opp.odt.builtin.ArgumentValidator;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatOperator extends BuiltInStringOperator {
    private static final Pattern REPLACEMENT_FLAGS = Pattern.compile("%s|%d");
    private FormatOperator() { }
    public static final FormatOperator INSTANCE = new FormatOperator();
    private static final List<String> PARAMETER_NAMES = List.of("format", "substitutes");

    @Override
    public String getName() {
        return "FORMAT";
    }

    @Override
    public int maxNumberOfArguments() {
        return -1;
    }

    @Override
    protected void specificValidation(PsiCall call, ProblemsHolder holder) {
        String signatureValue = call.getSignatureValue(0);
        if (signatureValue != null) {
            Matcher matcher = REPLACEMENT_FLAGS.matcher(signatureValue);
            List<String> replacementFlags = new ArrayList<>();
            while (matcher.find()) {
                replacementFlags.add(matcher.group());
            }
            validateFlags(call, holder, replacementFlags);
        }
    }

    private void validateFlags(PsiCall call, ProblemsHolder holder, List<String> replacementFlags) {
        if (!replacementFlags.isEmpty()) {
            if (replacementFlags.size() != call.getNumberOfArguments() - 1) {
                holder.registerProblem(
                        call,
                        "Unequal number of placeholders and arguments",
                        ProblemHighlightType.ERROR);
                return;
            }
            for (int i = 1; i < call.getNumberOfArguments(); i++) {
                String flag = replacementFlags.get(i - 1);
                if ("%s".equals(flag)) {
                    ArgumentValidator.validateStringArgument(i, call, holder);
                } else {
                    ArgumentValidator.validateNumberArgument(i, call, holder);
                }
            }
        }
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
