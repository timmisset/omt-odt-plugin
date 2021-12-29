package com.misset.opp.resolvable.util;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.psi.PsiCall;

import java.util.List;

public class CallableUtil {

    private static boolean passesMinArguments(Callable callable, int numberOfArguments) {
        return numberOfArguments >= callable.minNumberOfArguments();
    }

    private static boolean passesMaxArguments(Callable callable, int numberOfArguments) {
        return callable.maxNumberOfArguments() >= numberOfArguments || callable.maxNumberOfArguments() == -1;
    }

    private static String getExpectedArgumentsMessage(Callable callable) {
        int minNumberOfArguments = callable.minNumberOfArguments();
        int maxNumberOfArguments = callable.maxNumberOfArguments();
        if (minNumberOfArguments == 0) {
            if (maxNumberOfArguments > -1) {
                return "at most " + maxNumberOfArguments + " arguments";
            } else {
                return "no arguments";
            }
        } else if (minNumberOfArguments > 0) {
            if (maxNumberOfArguments == -1) {
                return "at least " + minNumberOfArguments + " arguments";
            } else {
                return "between " + minNumberOfArguments + " and " + maxNumberOfArguments + " arguments";
            }
        }
        return null;
    }

    public static void validateCallArguments(Callable callable, PsiCall call, ProblemsHolder holder) {
        final int numberOfArguments = call.getNumberOfArguments();
        if (!passesMinArguments(callable, numberOfArguments) ||
                !passesMaxArguments(callable, numberOfArguments)) {
            PsiElement callSignatureElement = call.getCallSignatureElement();
            holder.registerProblem(callSignatureElement != null ? callSignatureElement : call,
                    "Expects " + getExpectedArgumentsMessage(callable) + " arguments. Call has " + numberOfArguments + " arguments",
                    ProblemHighlightType.ERROR);
        }
    }

    public static void validateCallFlag(Callable callable, PsiCall call, ProblemsHolder holder) {
        String flag = call.getFlag();
        List<String> flags = callable.getFlags();
        if (flag != null && !flags.contains(flag)) {
            holder.registerProblem(call.getFlagElement(),
                    "Illegal flag, options are: " + String.join(", ", flags),
                    ProblemHighlightType.ERROR);
        }
    }

}
