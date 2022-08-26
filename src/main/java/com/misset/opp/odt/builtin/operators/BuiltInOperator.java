package com.misset.opp.odt.builtin.operators;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.Pair;
import com.misset.opp.odt.builtin.Builtin;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.model.OppModelConstants;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.apache.jena.ontology.OntResource;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public abstract class BuiltInOperator extends Builtin {

    protected static final List<String> IGNORE_CASE_FLAG = List.of("!ignoreCase");

    @Override
    public boolean isCommand() {
        return false;
    }

    @Override
    public boolean isVoid() {
        return false;
    }

    public String getCallId() {
        return getName();
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    protected void validateIgnoreCaseFlagUsage(int dynamicArgumentIndex,
                                               PsiCall call,
                                               ProblemsHolder holder) {
        validateFlagUsage(dynamicArgumentIndex, call, holder, "true", "ignoreCase");
    }

    protected void validateFlagUsage(int dynamicArgumentIndex,
                                     PsiCall call,
                                     ProblemsHolder holder,
                                     String defaultValueForDynamicArgument,
                                     String flagName) {
        if (call.getFlag() != null && call.getNumberOfArguments() == dynamicArgumentIndex + 1) {
            holder.registerProblem(call.getCallSignatureArgumentElement(dynamicArgumentIndex),
                    "Using both a flag and parameter",
                    ProblemHighlightType.ERROR);
        }
        if (call.getNumberOfArguments() == dynamicArgumentIndex + 1) {
            String signatureValue = call.getSignatureValue(dynamicArgumentIndex);
            if (defaultValueForDynamicArgument.equals(signatureValue)) {
                holder.registerProblem(call.getCallSignatureArgumentElement(dynamicArgumentIndex),
                        "Use !" + flagName + " flag instead",
                        ProblemHighlightType.WEAK_WARNING);
            }
        }
    }
    protected void validateIgnoreCaseFlagIsUsedOnStrings(Set<OntResource> resources,
                                                         PsiCall call,
                                                         ProblemsHolder holder) {
        String flag = call.getFlag();
        if (!resources.isEmpty() &&
                flag != null &&
                flag.contains("!ignoreCase") &&
                !resources.contains(OppModelConstants.XSD_STRING_INSTANCE)) {
            holder.registerProblem(call.getFlagElement(), "Using ignoreCase on non-string values", ProblemHighlightType.WARNING);
        }
    }

    /**
     * Validates that left-hand and right-hand arguments are compatible by extracting the left-hand and
     * right-hand based on the number of arguments. 1 == input + arg(0), 2 == arg(0) + arg(1).
     * Returns set from left-hand for additional type validations
     */
    protected Set<OntResource> validateLeftRightCompatible(PsiCall call, ProblemsHolder holder) {
        Pair<Set<OntResource>, Set<OntResource>> leftRight = getLeftRightFromArguments(call);
        if(leftRight.getFirst() == null || leftRight.getSecond() == null) {
            return Collections.emptySet();
        }
        TTLValidationUtil.validateCompatibleTypes(leftRight.getFirst(), leftRight.getSecond(), holder, call);
        return leftRight.getFirst();
    }
    protected Pair<Set<OntResource>, Set<OntResource>> getLeftRightFromArguments(PsiCall call) {
        final Set<OntResource> left;
        final Set<OntResource> right;
        if (call.getNumberOfArguments() == 1) {
            left = call.resolvePreviousStep();
            right = call.resolveSignatureArgument(0);
        } else if (call.getNumberOfArguments() == 2) {
            left = call.resolveSignatureArgument(0);
            right = call.resolveSignatureArgument(1);
        } else {
            return Pair.empty();
        }
        return Pair.create(left, right);
    }

    protected void validateCompatibleOutcomePossibilities(Pair<Set<OntResource>, Set<OntResource>> possibilities,
                                                          PsiCall call,
                                                          ProblemsHolder holder) {
        if (possibilities.getFirst().isEmpty() || possibilities.getSecond().isEmpty()) {
            return;
        }

        boolean b = OppModel.INSTANCE.areCompatible(possibilities.getFirst(), possibilities.getSecond());
        if (!b) {
            holder.registerProblem(call,
                    "Possible outcomes are incompatible, not illegal but it smells fishy",
                    ProblemHighlightType.WEAK_WARNING);
        }
    }

    @Override
    public String getType() {
        return "Builtin Operator";
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return true;
    }

    @Override
    protected List<String> getParameters() {
        return Collections.emptyList();
    }
}
