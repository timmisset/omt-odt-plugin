package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.psi.PsiNamedElement;
import com.misset.opp.callable.Callable;
import com.misset.opp.odt.documentation.ODTDocumented;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.ODTFlagSignature;
import com.misset.opp.odt.psi.ODTSignature;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface ODTCall extends PsiNamedElement, ODTResolvable, ODTDocumented {
    @Nullable
    Callable getCallable();

    @NotNull
    ODTCallName getCallName();

    @Nullable
    ODTFlagSignature getFlagSignature();

    @Nullable
    ODTSignature getSignature();

    @NotNull
    List<ODTSignatureArgument> getSignatureArguments();

    @Nullable
    ODTSignatureArgument getSignatureArgument(int index);

    /**
     * Returns the resolved OntResource set at the given index
     * Does not throw out-of-bounds exception but always returns an empty set when index is not present
     */
    @NotNull
    Set<OntResource> resolveSignatureArgument(int index);
}