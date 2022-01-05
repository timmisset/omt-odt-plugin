package com.misset.opp.odt.psi.impl.resolvable.util;

import com.intellij.lang.annotation.AnnotationBuilder;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.misset.opp.odt.syntax.ODTSyntaxHighlighter;
import com.misset.opp.ttl.util.TTLResourceUtil;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ODTResolvableUtil {

    private static final Logger LOGGER = Logger.getInstance(ODTResolvableUtil.class);

    public static String getDocumentation(Set<OntResource> resources) {
        return getDocumentation(resources, true);
    }

    public static String getDocumentation(Set<OntResource> resources, boolean asHtmlList) {
        String prefix = asHtmlList ? "<ul><li>" : "";
        String suffix = asHtmlList ? "</li></ul>" : "";
        String joining = asHtmlList ? "</li><li>" : "<br>";
        if (resources.isEmpty()) {
            return "Unknown";
        }
        return prefix + resources.stream()
                .sorted(Comparator.comparing(Resource::toString))
                .map(TTLResourceUtil::describeUri)
                .filter(Objects::nonNull)
                .collect(Collectors.joining(joining)) + suffix;
    }

    public static void annotateResolved(Set<OntResource> resources,
                                        AnnotationHolder holder,
                                        PsiElement range,
                                        boolean applyTextAttributes) {
        LoggerUtil.runWithLogger(LOGGER,
                "Annotating resolved resources, n=" + Optional.ofNullable(resources).map(Set::size).orElse(0),
                () -> {
                    if (resources == null || holder == null || resources.isEmpty()) {
                        return;
                    }
                    final AnnotationBuilder builder = LoggerUtil.computeWithLogger(LOGGER,
                            "Annotating resolved resources, building annotation",
                            () -> holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                    .range(range));

                    LoggerUtil.runWithLogger(LOGGER, "Annotating resolved resources, setting text attributes", () -> {
                        if (applyTextAttributes) {
                            final TextAttributesKey ontologyTextAttributesKey = getOntologyTextAttributesKey(resources);
                            if (ontologyTextAttributesKey != null) {
                                // not ignored, it is not required to use the returned value
                                // we cannot use it because builder has to remain final
                                builder.textAttributes(ontologyTextAttributesKey);
                            }
                        }
                        builder.create();
                    });

                });
    }

    private static TextAttributesKey getOntologyTextAttributesKey(Set<OntResource> resources) {
        if (resources.stream().allMatch(TTLResourceUtil::isValue)) {
            return ODTSyntaxHighlighter.OntologyValueAttributesKey;
        }
        if (resources.stream().allMatch(TTLResourceUtil::isIndividual)) {
            return ODTSyntaxHighlighter.OntologyInstanceAttributesKey;
        }
        if (resources.stream().allMatch(TTLResourceUtil::isType)) {
            return ODTSyntaxHighlighter.OntologyTypeAttributesKey;
        }
        if (resources.stream().allMatch(TTLResourceUtil::isClass)) {
            return ODTSyntaxHighlighter.OntologyClassAttributesKey;
        }
        return null;
    }

}
