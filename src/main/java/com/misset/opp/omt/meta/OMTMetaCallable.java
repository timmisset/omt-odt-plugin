package com.misset.opp.omt.meta;

import com.intellij.codeInspection.ProblemsHolder;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;

/**
 * The OMTCallableImpl interface can be implemented on OMTMetaType classes
 * These should be able to resolve a call made to them with a set of resources
 * <p>
 * The OMTMetaCallable doesn't extend the Callable interface by design. MetaTypes are not
 * part of the PsiTree and need to be called with their corresponding PsiElement
 * to be able to answer the OMTCallable interface methods.
 * <p>
 * The OMTCallableImpl serves as the proxy that provides the PsiElement to the different
 * MetaTypes.
 */
public interface OMTMetaCallable {

    /**
     * Resolve the call with a base set of resource and the call with parameters
     */
    Set<OntResource> resolve(YAMLMapping mapping, @Nullable Context context);

    boolean isVoid(YAMLMapping mapping);

    /**
     * Checks if the Callable type can be applied (as query/operator) on the input
     */
    boolean canBeAppliedTo(YAMLMapping mapping, Set<OntResource> resources);

    Set<OntResource> getSecondReturnArgument();

    default List<String> getFlags() {
        return Collections.emptyList();
    }

    default Set<String> getAcceptableValues(int index) {
        return Collections.emptySet();
    }

    int minNumberOfArguments(YAMLMapping mapping);

    int maxNumberOfArguments(YAMLMapping mapping);

    Map<Integer, String> getParameterNames(YAMLMapping mapping);

    HashMap<Integer, Set<OntResource>> getParameterTypes(YAMLMapping mapping);

    default void validate(YAMLMapping mapping, PsiCall call, ProblemsHolder holder) {
    }

    default void validateValue(PsiCall call, ProblemsHolder holder, int i) {
    }
}
