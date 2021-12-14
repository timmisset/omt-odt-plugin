package com.misset.opp.omt.meta;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    Set<OntResource> resolve(YAMLMapping mapping,
                             Set<OntResource> resources,
                             Call call);

    boolean isVoid(YAMLMapping mapping);

    /**
     * Checks if the Callable type can be applied (as query/operator) on the input
     */
    boolean canBeAppliedTo(YAMLMapping mapping, Set<OntResource> resources);

    Set<OntResource> getSecondReturnArgument();

    String getType();

    default List<String> getFlags() {
        return Collections.emptyList();
    }
}
