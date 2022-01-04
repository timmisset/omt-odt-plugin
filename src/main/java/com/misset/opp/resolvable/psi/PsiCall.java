package com.misset.opp.resolvable.psi;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * PsiElement with a Callable counterpart
 * It provides context to the Callable resolve method to determine the outcome and provides
 * information about the number and type of arguments and the flag (if any) used when making
 * the call.
 * A call can contain 0 to many signature arguments and be called with or without a flag.
 * Example:
 * 'Hi there' / CONTAINS!ignoreCase('hi');
 */
public interface PsiCall extends PsiResolvable {

    /**
     * Returns the type(s) of the value at given index of the signature
     * CONTAINS!ignoreCase('hi') -> returns Set.of(xsd:string) for index 0
     */
    @NotNull
    Set<OntResource> resolveSignatureArgument(int index);

    /**
     * Returns a list with the types for all arguments in the same order
     * of the signature arguments
     * CALL('foo', 1) -> returns List.of(Set.of(xsd:string), Set.of(xsd:integer))
     */
    @NotNull
    List<Set<OntResource>> resolveSignatureArguments();

    /**
     * Returns the exact string value of the argument:
     * CONTAINS!ignoreCase('hi') -> returns "'hi'" for index 0
     */
    @Nullable
    String getSignatureValue(int index);

    /**
     * Returns a list with the string values of all signatures
     * CALL('foo', 1) -> returns List.of("'hi'", "1")
     */
    List<String> getSignatureValues();

    /**
     * Returns the flag used in the call
     * CONTAINS!ignoreCase('hi') -> returns "!ignoreCase"
     */
    @Nullable
    String getFlag();

    /**
     * Returns the total number of arguments used in the call
     */
    int getNumberOfArguments();

    /**
     * Provides context-information about parameters that are part of a greater resolve operation.
     * These are not parameters that are part of the call itself.
     * <p>
     * For example, a base parameter for a StandaloneQuery that must be known when resolving the
     * call made to the query.
     */
    Set<OntResource> getParamType(String paramName);

    /**
     * Adds context-information about parameters that are part of a greater resolve operation.
     * These are not parameters that are part of the call itself.
     * <p>
     * For example, a base parameter for a StandaloneQuery that must be known when resolving the
     * call made to the query.
     */
    void setParamType(String paramName,
                      Set<OntResource> type);

    /**
     * Returns the callId that should exactly match the callId of the compatible Callable
     * for example @Call($param) will return @Call
     */
    String getCallId();

    /**
     * Returns the name of the call, for example @Call($param) will return Call
     */
    String getName();

    /**
     * Returns the Signature PsiElement. Used to narrow the scope of validation Errors, Warnings etc
     */
    PsiElement getCallSignatureElement();

    /**
     * Returns the SignatureArgument PsiElement at the given index.
     * Used to narrow the scope of validation Errors, Warnings etc
     */
    PsiElement getCallSignatureArgumentElement(int index);

    /**
     * Returns the Flag PsiElement.
     * Used to narrow the scope of validation Errors, Warnings etc
     */
    PsiElement getFlagElement();

    /**
     * Returns the information leading up to the resolved value
     * For example:
     * Call(/ont:ClassA / ^rdf:type / ont:property)
     * The call argument resolves to the Object
     * The getSignatureLeadingInformation returns the Subject (/ont:ClassA / ^rdf:type) resolved and Predicate (ont:property)
     * <p>
     * This method will only be able to resolve anything when call is a Query with atleast a subject and predicate
     * for example:
     * Call($variable) => only has a subject, cannot obtain any information => returns null
     * Call($variable / ont:property) => has subject and predicate => returns the pair
     * <p>
     * Returns null when ambiguous or not able to resolve
     */
    @Nullable Pair<Set<OntResource>, Property> getSignatureLeadingInformation(int signatureArgument);

    /**
     * Used for Refactoring safe-delete. When the Callable (as PsiElement) is not using a parameter, it can remove it
     * and remove all parameters from calls made to the Callable also.
     */
    void removeArgument(int index);

    /**
     * Operators are applied within a path where it behaves as a pipe, taking in a value, operates on it and
     * returns a value. The getCallInputType provides the input type for the operator.
     */
    Set<OntResource> resolvePreviousStep();
}
