package com.misset.opp.omt.meta;

import com.misset.opp.callable.Call;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.Set;

/**
 * The OMTCallableImpl interface can be implemented on OMTMetaType classes
 * These should be able to resolve a call made to them with a set of parameters
 * <p>
 * The OMTMetaCallable doesn't extend the Callable interface by design. MetaTypes can implement only this
 * interface for example. The OMTMetaCallable can duplicate interface methods from the Callable when
 * the MetaType is the most suitable to provide a specific answer. For example, isVoid() is specific to
 * the type of OMTModelItem and can be answered without any PsiElement input.
 */
public interface OMTMetaCallable {

    /**
     * Resolve the OMTCallableImpl, types are asserted from annotations on the structure itself
     */
    Set<OntResource> resolve(YAMLMapping mapping,
                             Set<OntResource> resources,
                             Call call);

    boolean isVoid();
}
