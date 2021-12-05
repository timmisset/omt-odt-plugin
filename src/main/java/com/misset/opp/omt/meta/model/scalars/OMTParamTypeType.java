package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTInjectable;
import com.misset.opp.omt.meta.providers.util.OMTPrefixProviderUtil;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLValueParserUtil;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

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

    public OMTParamTypeType() {
        super("OMTParamTypeType");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {

    }

    public @Nullable TextRange getTypePrefixRange(YAMLPlainTextImpl value) {
        final Matcher matcher = CURIE_PATTERN.matcher(value.getText());
        final boolean b = matcher.find();
        if (b && matcher.groupCount() == 2 && matcher.start(1) > -1) {
            return new TextRange(matcher.start(1), matcher.end(1));
        }
        return null;
    }

    /**
     * Resolves the type that is present as string
     *
     * @param type    the type part of the declaration (the part between parenthesis if shorthanded, otherwise the value from the type key)
     * @param element the element (YAMLValue) that contains the type
     */
    public static Set<OntResource> resolveType(PsiElement element,
                                               String type) {
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
    }
}
