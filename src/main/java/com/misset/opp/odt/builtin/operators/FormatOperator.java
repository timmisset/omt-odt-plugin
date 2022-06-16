package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatOperator extends BuiltInStringOperator {
    private static final Pattern REPLACEMENT_FLAGS = Pattern.compile("%s|%d");
    private FormatOperator() { }
    public static final FormatOperator INSTANCE = new FormatOperator();

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
        if(signatureValue != null) {
            Matcher matcher = REPLACEMENT_FLAGS.matcher(signatureValue);
            List<String> replacementFlags = new ArrayList<>();
            while(matcher.find()) {
                replacementFlags.add(matcher.group());
            }
            if(replacementFlags.size() > 0) {
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
                        validateStringArgument(i, call, holder);
                    } else {
                        validateNumberArgument(i, call, holder);
                    }
                }
            }
        }
    }

    @Override
    public Set<OntResource> getAcceptableArgumentTypeWithContext(int index, PsiCall call) {
        if (index == 0) {
            return Set.of(OppModelConstants.XSD_STRING_INSTANCE);
        }
        return null;
    }
}
