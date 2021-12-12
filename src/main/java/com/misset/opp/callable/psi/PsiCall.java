package com.misset.opp.callable.psi;

import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

/**
 * Any PsiElement that is callable should use this interface to indicate that it's also a valid PsiElement
 */
public interface PsiCall extends Call, PsiElement {

    PsiElement getCallSignatureElement();

    PsiElement getCallSignatureArgumentElement(int index);

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
    default @Nullable Pair<Set<OntResource>, Property> getSignatureLeadingInformation(int signatureArgument) {
        return null;
    }

    /**
     * Operators are applied within a path where it behaves as a pipe, taking in a value, operates on it and
     * returns a value. The getCallInputType provides the input type for the operator. It's equal to the
     * outcome of the previous step in the path being resolved
     */
    default Set<OntResource> resolveCallInput() {
        return Collections.emptySet();
    }

}
