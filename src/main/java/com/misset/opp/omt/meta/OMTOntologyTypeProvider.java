package com.misset.opp.omt.meta;

import com.intellij.openapi.util.TextRange;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

/**
 * Able to resolve the YAMLPlainTextImpl content of the PsiElement into a fully Qualified URI.
 * For example:
 * $param (ont:ClassA) returns http://ontology/ClassA
 * $variable = 1 returns http://www.w3.org/2001/XMLSchema#integer
 */
public interface OMTOntologyTypeProvider {

    String getFullyQualifiedURI(YAMLPlainTextImpl value);

    TextRange getOntologyTypeTextRange(YAMLPlainTextImpl value);

}
