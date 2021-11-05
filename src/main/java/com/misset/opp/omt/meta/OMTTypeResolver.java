package com.misset.opp.omt.meta;

import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.Set;

public interface OMTTypeResolver {
    Set<OntResource> getType(YAMLPsiElement element);
    Set<OntResource> getTypeFromDestructed(YAMLMapping mapping);
}
