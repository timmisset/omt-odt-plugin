package com.misset.opp.odt.psi.resolvable.callable;

import com.intellij.psi.PsiNameIdentifierOwner;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.resolvable.ODTResolvable;
import com.misset.opp.refactoring.SupportsSafeDelete;
import com.misset.opp.resolvable.psi.PsiCallable;
import org.apache.jena.ontology.OntResource;

import java.util.Set;

public interface ODTDefineStatement extends ODTResolvable, PsiCallable, SupportsSafeDelete, PsiNameIdentifierOwner {

    ODTDefineName getDefineName();

    ODTDefineParam getDefineParam();

    Set<OntResource> getReturnType();

}
