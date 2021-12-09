package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTInjectable;
import com.misset.opp.omt.meta.providers.util.OMTPrefixProviderUtil;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValueParserUtil;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The Meta-type for a OMTParamMetaType
 */
public class OMTParamTypeType extends YamlScalarType implements OMTInjectable {

    private static final Pattern CURIE_PATTERN = Pattern.compile("([A-z0-9]+):([A-z0-9]+)");
    private static final Logger LOGGER = Logger.getInstance(OMTParamTypeType.class);

    public OMTParamTypeType() {
        super("OMTParamTypeType");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {

    }

    /**
     * Resolves the type that is present as string
     *
     * @param type    the type part of the declaration (the part between parenthesis if shorthanded, otherwise the value from the type key)
     * @param element the element (YAMLValue) that contains the type
     */
    public static Set<OntResource> resolveType(PsiElement element,
                                               String type) {
        return LoggerUtil.computeWithLogger(LOGGER, "Calculating param type for " + type, () -> {
            final Matcher matcher = CURIE_PATTERN.matcher(type);
            final boolean b = matcher.find();
            if (b) {
                // it's a  curie, try to resolve via the prefix:
                String prefix = type.substring(matcher.start(1), matcher.end(1));
                String localName = type.substring(matcher.start(2), matcher.end(2));
                final String uri = OMTPrefixProviderUtil.resolveToFullyQualifiedUri(element, prefix, localName);
                return OppModel.INSTANCE.getClassIndividuals(uri)
                        .stream()
                        .map(OntResource.class::cast)
                        .collect(Collectors.toSet());
            } else {
                // not a curie, try to resolve as simple type:
                return Optional.ofNullable(TTLValueParserUtil.parsePrimitive(type))
                        .map(OntResource.class::cast)
                        .map(Set::of)
                        .orElse(Collections.emptySet());
            }
        });
    }
}
