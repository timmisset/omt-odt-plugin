package com.misset.opp.omt.meta.model.scalars;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.misset.opp.omt.indexing.OMTPrefixIndex;
import com.misset.opp.omt.inspection.quickfix.OMTRegisterPrefixLocalQuickFix;
import com.misset.opp.omt.meta.providers.util.OMTPrefixProviderUtil;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.references.OMTParamTypeReference;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import com.misset.opp.ttl.util.TTLValueParserUtil;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.meta.model.CompletionContext;
import org.jetbrains.yaml.meta.model.YamlScalarType;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * The Meta-type for a OMTParamMetaType
 */
public class OMTParamTypeType extends YamlScalarType {

    private static final Pattern CURIE_PATTERN = Pattern.compile("([A-z0-9]+):([A-z0-9]+)");
    private static final Pattern URI_PATTERN = Pattern.compile("<([^\\s]*)>");
    private static final Pattern SIMPLE_PATTERN = Pattern.compile("[a-z]+");
    private static final Logger LOGGER = Logger.getInstance(OMTParamTypeType.class);

    protected static final String UNKNOWN_PREFIX = "Unknown prefix";

    public OMTParamTypeType() {
        super("OMTParamTypeType");
    }

    public static OMTParamTypeReference getReference(YAMLPlainTextImpl plainText) {
        Matcher matcher = CURIE_PATTERN.matcher(plainText.getTextValue());
        boolean b = matcher.find();
        if (!b) {
            return null;
        }

        return new OMTParamTypeReference(plainText, TextRange.create(matcher.start(1), matcher.end(1)));
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalarValue,
                                       @NotNull ProblemsHolder holder) {
        if (!isUri(scalarValue) & !isCurie(scalarValue) && !SIMPLE_PATTERN.matcher(scalarValue.getTextValue()).find()) {
            holder.registerProblem(scalarValue, "Syntax error, match either: " + CURIE_PATTERN.pattern() + " or " + URI_PATTERN.pattern() + " or " + SIMPLE_PATTERN.pattern(), ProblemHighlightType.ERROR);
            return;
        }
        if (uri != null) {
            Resource resource = ResourceFactory.createResource(uri);
            String namespace = resource.getNameSpace();
            String localName = resource.getLocalName();
            if (namespace == null || localName == null) {
                holder.registerProblem(scalarValue,
                        "Syntax error, not a valid URI",
                        ProblemHighlightType.ERROR);
                return;
            }
            List<String> prefixes = OMTPrefixIndex.getPrefixes(namespace);
            if (!prefixes.isEmpty()) {
                holder.registerProblem(scalarValue, "Using fully qualified URI",
                        ProblemHighlightType.WARNING,
                        prefixes.stream()
                                .map(prefix -> getQuickFix(prefix, namespace, localName))
                                .toArray(LocalQuickFix[]::new));
            }
            validateResource(uri, scalarValue, holder);
        } else if (curie != null) {
            OMTFile containingFile = (OMTFile) scalarValue.getContainingFile();
            Map<String, String> availableNamespaces = containingFile.getAvailableNamespaces(scalarValue);
            String namespace = availableNamespaces.entrySet()
                    .stream()
                    .filter(stringStringEntry -> stringStringEntry.getValue().equals(prefix))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            if (namespace == null) {
                validatePrefixReference((OMTParamTypeReference) scalarValue.getReference(), holder);
            } else {
                validateResource(namespace + localname, scalarValue, holder);
            }
        }
    }

    public static void validatePrefixReference(OMTParamTypeReference reference, ProblemsHolder holder) {
        if (reference == null || reference.resolve() != null) {
            return;
        }
        YAMLPlainTextImpl element = reference.getElement();
        String text = element.getText();
        String prefix = reference.getRangeInElement().substring(text);

        LocalQuickFix[] localQuickFixes = OMTPrefixIndex.getNamespaces(prefix).stream()
                .map(iri -> new OMTRegisterPrefixLocalQuickFix(prefix, iri))
                .toArray(LocalQuickFix[]::new);
        holder.registerProblem(
                element,
                UNKNOWN_PREFIX,
                ProblemHighlightType.ERROR,
                reference.getRangeInElement(),
                localQuickFixes);
    }

    private void validateResource(String uri, PsiElement element, ProblemsHolder holder) {
        OntResource ontResource = OppModel.INSTANCE.getOntResource(uri, holder.getProject());
        if (ontResource == null) {
            holder.registerProblem(element, "Could not find resource <" + uri + "> in the Opp Model", ProblemHighlightType.ERROR);
        } else if (!TTLResourceUtil.isType(ontResource) && !TTLResourceUtil.isClass(ontResource)) {
            holder.registerProblem(element, "Expected a class or type", ProblemHighlightType.ERROR);
        }
    }

    private String uri = null;

    private boolean isUri(YAMLScalar scalarValue) {
        Matcher matcher = URI_PATTERN.matcher(scalarValue.getTextValue());
        boolean b = matcher.find();
        if (b) {
            uri = matcher.group(1);
        }
        return b;
    }

    private String curie;
    private String localname;
    private String prefix;

    private boolean isCurie(YAMLScalar scalarValue) {
        Matcher matcher = CURIE_PATTERN.matcher(scalarValue.getTextValue());
        boolean b = matcher.find();
        if (b) {
            curie = matcher.group();
            prefix = matcher.group(1);
            localname = matcher.group(2);
        }
        return b;
    }

    @Override
    public @NotNull List<? extends LookupElement> getValueLookups(@NotNull YAMLScalar insertedScalar, @Nullable CompletionContext completionContext) {
        // add completions for classes and types:
        PsiFile containingFile = insertedScalar.getContainingFile();
        if (containingFile instanceof OMTFile) {
            ArrayList<LookupElementBuilder> list = OppModel.INSTANCE.listClasses().stream()
                    .map(ontClass -> TTLResourceUtil.getTypeLookupElement(ontClass, ((OMTFile) containingFile)
                            .getAvailableNamespaces(insertedScalar)))
                    .collect(Collectors.toCollection(ArrayList::new));
            list.add(LookupElementBuilder.create("string"));
            list.add(LookupElementBuilder.create("boolean"));
            list.add(LookupElementBuilder.create("integer"));
            list.add(LookupElementBuilder.create("decimal"));
            list.add(LookupElementBuilder.create("date"));
            list.add(LookupElementBuilder.create("dateTime"));
            list.add(LookupElementBuilder.create("number"));
            return list;
        }
        return Collections.emptyList();
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

    private LocalQuickFix getQuickFix(String prefix, String namespace, String localName) {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Register prefix";
            }

            @Override
            public @IntentionName @NotNull String getName() {
                return "Register as " + prefix;
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                // register the prefix
                PsiElement psiElement = descriptor.getPsiElement();

                // replace the current element:
                YAMLValue newValue = YAMLElementGenerator.getInstance(project).createYamlKeyValue("foo", prefix + ":" + localName)
                        .getValue();
                if (newValue != null) {
                    psiElement = psiElement.replace(newValue);
                }
                new OMTRegisterPrefixLocalQuickFix(prefix, namespace).addPrefix(project, psiElement);
            }
        };
    }
}
