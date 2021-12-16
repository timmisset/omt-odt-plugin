package com.misset.opp.ttl.psi.iri;

import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.Nullable;

public interface TTLIriHolder extends PsiElement {
    @Nullable String getQualifiedUri();
}
