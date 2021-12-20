package com.misset.opp.callable.psi;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Callable;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import com.misset.opp.ttl.util.TTLValidationUtil;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Any PsiElement that is callable should use this interface to indicate that it's also a valid PsiElement
 * If the PsiCallable wraps a PsiElement it should be returned using the getOriginalElement() method
 */
public interface PsiCallable extends Callable, PsiElement, PsiResolvable {
    Logger LOGGER = Logger.getInstance(PsiCallable.class);

    Set<OntResource> getParamType(int index);

    @Override
    default void validate(PsiCall call, ProblemsHolder holder) {
        Callable.super.validate(call, holder);
        LoggerUtil.runWithLogger(LOGGER,
                "Validation of call " + call.getCallId(),
                () -> {
                    for (int i = 0; i < call.numberOfArguments(); i++) {
                        int finalI = i;
                        Set<OntResource> argumentType = LoggerUtil.computeWithLogger(
                                LOGGER,
                                "Calculating signature argument " + i + " for " + call.getCallId(),
                                () -> call.resolveSignatureArgument(finalI));
                        Set<OntResource> paramType = LoggerUtil.computeWithLogger(
                                LOGGER,
                                "Calculating parameter " + i + " for " + getName(),
                                () -> getParamType(finalI));
                        if (paramType != null && !argumentType.isEmpty() && !paramType.isEmpty()) {
                            TTLValidationUtil.validateCompatibleTypes(paramType, argumentType, holder, call.getCallSignatureArgumentElement(i));
                        }
                    }
                });
    }

    @Override
    default String getDescription(String context) {
        Map<Integer, String> parameterNames = getParameterNames();
        HashMap<Integer, Set<OntResource>> parameterTypes = getParameterTypes();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(getName()).append("<br>")
                .append("type: ").append(getType()).append("<br>");
        if (maxNumberOfArguments() > 0) {
            stringBuilder.append("params:<br>");
        }
        for (int i = 0; i < maxNumberOfArguments(); i++) {
            String name = parameterNames.getOrDefault(i, "$param" + i);
            String type = TTLResourceUtil.describeUrisForLookupJoined(parameterTypes.getOrDefault(i, Set.of(OppModel.INSTANCE.OWL_THING_INSTANCE)));
            stringBuilder.append("- ").append(name);
            if (!type.isBlank()) {
                stringBuilder.append(" (").append(type).append(")");
            }
            stringBuilder.append("<br>");
        }
        return stringBuilder.toString();
    }
}
