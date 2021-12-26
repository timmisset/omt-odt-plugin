package com.misset.opp.resolvable;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Callable is any instance that can receive and resolve a call.
 * This interface is language and PsiTree independent. To prevent code duplication, it comes with
 * a set of default methods. The PsiTree class inheritance structure makes it impossible to use
 * an abstract class to provide default implementations for this interface.
 */
public interface Callable extends ContextResolvable {
    Logger LOGGER = Logger.getInstance(Callable.class);

    /**
     * The descriptive name of the callable item
     */
    String getName();

    /**
     * Returns the description for this callable item
     * The context might provide some additional information about the location of the call that can be included in the
     * output string. The returned string should be HTML based.
     */
    String getDescription(String context);

    /**
     * The id used to call this callable. For example, @Call for commands, Call for operators.
     */
    String getCallId();

    /**
     * The minimum number of arguments that the callable should be called with.
     * The actual number of arguments used is retrieved from Call
     *
     * @see com.misset.opp.resolvable.psi.PsiCall
     */
    int minNumberOfArguments();

    /**
     * The maximum number of arguments that the callable should be called with
     * The actual number of arguments used is retrieved from Call
     *
     * @see com.misset.opp.resolvable.psi.PsiCall
     */
    int maxNumberOfArguments();

    /**
     * If this callable returns any value (false) or is void (true)
     */
    boolean isVoid();

    /**
     * If this callable is considered a Command (true) or Operator (false)
     */
    boolean isCommand();

    /**
     * The flags with which this callable can be called.
     * The actual flags used are retrieved from Call
     *
     * @see com.misset.opp.resolvable.psi.PsiCall
     */
    default List<String> getFlags() {
        return Collections.emptyList();
    }

    /**
     * If the callable returns more than 1 argument. In which case both arguments
     * should be an implementation of Set<OntResource>.
     * This is used to determine if variable assignments that are made using the return values
     * of this callable are applicable. For example:
     * $variableA, $variableB = @SomeCall();
     * <p>
     * Currently, only a primary return argument (returns)
     * or second return argument (getSecondReturnArgument) is supported
     */
    default Set<OntResource> getSecondReturnArgument() {
        return Collections.emptySet();
    }

    @Override
    default @NotNull Set<OntResource> resolve() {
        return Collections.emptySet();
    }

    /**
     * Returns true of the callable can be placed anywhere without any leading information
     * usually true for all commands but also queries and some operators can be static.
     */
    boolean isStatic();

    /**
     * Default method to validate a call made by a PsiCall
     */
    default void validate(PsiCall call,
                          ProblemsHolder holder) {
        LoggerUtil.runWithLogger(LOGGER,
                "Validation of call " + call.getCallId(),
                () -> {
                    validateCallArguments(call, holder);
                    validateCallFlag(call, holder);
                });

    }

    private void validateCallArguments(PsiCall call, ProblemsHolder holder) {
        final int numberOfArguments = call.getNumberOfArguments();
        if (!passesMinArguments(numberOfArguments) || !passesMaxArguments(numberOfArguments)) {
            PsiElement callSignatureElement = call.getCallSignatureElement();
            holder.registerProblem(callSignatureElement != null ? callSignatureElement : call,
                    "Expects " + getExpectedArgumentsMessage() + " arguments. Call has " + numberOfArguments + " arguments",
                    ProblemHighlightType.ERROR);
        }
    }

    private void validateCallFlag(PsiCall call, ProblemsHolder holder) {
        String flag = call.getFlag();
        if (flag != null && !getFlags().contains(flag)) {
            holder.registerProblem(call.getFlagElement(),
                    "Illegal flag, options are: " + String.join(", ", getFlags()),
                    ProblemHighlightType.ERROR);
        }
    }

    /**
     * Returns a map with the resolved parameter types by index
     * Empty set should be returned when unresolvable
     */
    HashMap<Integer, Set<OntResource>> getParameterTypes();

    /**
     * Returns a map with the parameter names by index
     * $param[index] should be returned when unnamed
     */
    Map<Integer, String> getParameterNames();

    /**
     * The acceptable types at the given position, taking the context of the Callable into consideration
     * For example, if @ADD_TO is called with a String type for the collection, the argument should be a string to.
     * Should return Set.of(OppModel.INSTANCE.OWL_THING_INSTANCE) when any input is acceptable
     */
    default Set<OntResource> getAcceptableArgumentType(int index, PsiCall call) {
        return getParameterTypes().getOrDefault(index, Set.of(OppModel.INSTANCE.OWL_THING_INSTANCE));
    }

    /**
     * The acceptable input for this Callable
     * Should return Set.of(OppModel.INSTANCE.OWL_THING_INSTANCE) when any input is acceptable
     */
    default Set<OntResource> getAcceptableInputType() {
        return Set.of(OppModel.INSTANCE.OWL_THING_INSTANCE);
    }

    private boolean passesMinArguments(int numberOfArguments) {
        return numberOfArguments >= minNumberOfArguments();
    }

    private boolean passesMaxArguments(int numberOfArguments) {
        return maxNumberOfArguments() >= numberOfArguments || maxNumberOfArguments() == -1;
    }

    private String getExpectedArgumentsMessage() {
        if (minNumberOfArguments() == 0) {
            if (maxNumberOfArguments() > -1) {
                return "at most " + maxNumberOfArguments() + " arguments";
            } else {
                return "no arguments";
            }
        } else if (minNumberOfArguments() > 0) {
            if (maxNumberOfArguments() == -1) {
                return "at least " + minNumberOfArguments() + " arguments";
            } else {
                return "between " + minNumberOfArguments() + " and " + maxNumberOfArguments() + " arguments";
            }
        }
        return null;
    }

    default HashMap<Integer, Set<OntResource>> mapCallableParameters(List<Set<OntResource>> resources) {
        HashMap<Integer, Set<OntResource>> typeMapping = new HashMap<>();
        for (int i = 0; i < resources.size(); i++) {
            typeMapping.put(i, resources.get(i));
        }
        return typeMapping;
    }

    /**
     * The type of callable
     */
    String getType();

    /**
     * If the callable (as operator) can be applied on the resources.
     * Should return true if any value from resources is valid.
     */
    boolean canBeAppliedTo(Set<OntResource> resources);
}