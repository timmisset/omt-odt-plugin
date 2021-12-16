package com.misset.opp.ttl.psi;

import com.intellij.psi.PsiFile;

public interface TTLFile extends PsiFile {
    TTLSubject getSubject(String iri);

    TTLObject getPredicate(String subjectIri, String predicateIri);

    TTLObject getObject(String subjectIri, String predicateIri);
}
