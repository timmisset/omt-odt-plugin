package com.misset.opp.odt.psi.impl.resolvable.util;

import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.syntax.ODTSyntaxHighlighter;
import com.misset.opp.ttl.OppModel;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;

import java.util.Comparator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ODTResolvableUtil {

    public static void annotateResolved(Set<OntResource> resources,
                                        AnnotationHolder holder,
                                        PsiElement range,
                                        boolean applyTextAttributes) {
        if (resources == null || holder == null || resources.isEmpty()) {
            return;
        }
        AnnotationBuilder builder = holder.newAnnotation(HighlightSeverity.INFORMATION, "")
                .tooltip(
                        resources.stream()
                                .sorted(Comparator.comparing(Resource::toString))
                                .map(ODTResolvableUtil::describeUri)
                                .filter(Objects::nonNull)
                                .collect(Collectors.joining("<br>")))
                .range(range);

        if(applyTextAttributes) {
            final TextAttributesKey ontologyTextAttributesKey = getOntologyTextAttributesKey(resources);
            if (ontologyTextAttributesKey != null) {
                builder = builder.textAttributes(ontologyTextAttributesKey);
            }
        }
        builder.create();
    }

    private static TextAttributesKey getOntologyTextAttributesKey(Set<OntResource> resources) {
        if (resources.stream().allMatch(ODTResolvableUtil::isValue)) {
            return ODTSyntaxHighlighter.OntologyValueAttributesKey;
        }
        if (resources.stream().allMatch(OntResource::isIndividual)) {
            return ODTSyntaxHighlighter.OntologyInstanceAttributesKey;
        }
        if (resources.stream().allMatch(ODTResolvableUtil::isType)) {
            return ODTSyntaxHighlighter.OntologyTypeAttributesKey;
        }
        if (resources.stream().allMatch(ODTResolvableUtil::isClass)) {
            return ODTSyntaxHighlighter.OntologyClassAttributesKey;
        }
        return null;
    }

    private static boolean isType(OntResource resource) {
        return resource.isClass() &&  isXSDType(resource);
    }
    private static boolean isValue(OntResource resource) {
        return resource instanceof Individual && isXSDType(((Individual) resource).getOntClass());
    }
    private static boolean isXSDType(OntResource resource) {
        return resource.getNameSpace().equals(OppModel.XSD);
    }

    private static boolean isClass(OntResource resource) {
        return !isType(resource) && resource.isClass();
    }

    public static String describeUri(OntResource resource) {
        if (resource.isClass()) {
            if (resource.getNameSpace().equals(OppModel.XSD)) {
                return resource.getURI() + " (TYPE)";
            }
            return resource.getURI() + " (CLASS)";
        } else if (resource instanceof Individual) {
            final Individual individual = (Individual) resource;
            if (resource.getURI() == null) {
                return individual.getOntClass().getURI() + " (VALUE)";
            }
            return individual.getOntClass().getURI() + " (INSTANCE)";
        } else {
            return resource.getURI();
        }
    }

}
