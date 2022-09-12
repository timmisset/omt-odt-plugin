package com.misset.opp.odt.psi.resolvable.call;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.ODTFlagSignature;
import com.misset.opp.odt.psi.ODTSignature;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.reference.ODTCallReference;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import com.misset.opp.resolvable.psi.PsiCall;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public interface ODTCall extends PsiCall, PsiNamedElement, ODTResolvable {
    @NotNull
    ODTCallName getCallName();

    @Nullable
    ODTFlagSignature getFlagSignature();

    @Nullable
    ODTSignature getSignature();

    @NotNull
    List<ODTSignatureArgument> getSignatureArguments();

    ODTSignatureArgument getSignatureArgument(int index);

    /**
     * Returns the resolved OntResource set at the given index
     * Does not throw out-of-bounds exception but always returns an empty set when index is not present
     */
    @NotNull
    Set<OntResource> resolveSignatureArgument(int index);

    /**
     * Returns the argument index which contains the provided element
     */
    int getArgumentIndexOf(PsiElement element);

    ODTCallReference getReference();
}
