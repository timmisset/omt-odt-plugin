package com.misset.opp.odt.psi.impl.call;

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

    @NotNull
    Set<OntResource> resolveSignatureArgument(int index);
}
