package com.misset.opp.omt.meta.scalars;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.misset.opp.indexing.PrefixIndex;
import com.misset.opp.omt.inspection.quickfix.OMTRegisterPrefixLocalQuickFix;
import com.misset.opp.omt.meta.OMTOntologyTypeProvider;
import com.misset.opp.omt.meta.providers.util.OMTPrefixProviderUtil;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.references.OMTParamTypePrefixReference;
import com.misset.opp.omt.util.PatternUtil;
import com.misset.opp.ttl.model.OppModel;
import com.misset.opp.ttl.util.TTLResourceUtil;
import com.misset.opp.ttl.util.TTLValueParserUtil;
import org.apache.jena.ontology.OntClass;
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

public class OMTParamTypeType extends YamlScalarType implements OMTOntologyTypeProvider {

    public static final Pattern CURIE_PATTERN = Pattern.compile("([A-z0-9]+):([A-z0-9]+)");
    public static final Pattern URI_PATTERN = Pattern.compile("<([^\\s]*)>");
    private static final Pattern PRIMITIVE_PATTERN = Pattern.compile("[a-z]+");

    protected static final String UNKNOWN_PREFIX = "Unknown prefix";
    private static final OMTParamTypeType INSTANCE = new OMTParamTypeType();

    public static OMTParamTypeType getInstance() {
        return INSTANCE;
    }

    private OMTParamTypeType() {
        super("OMTParamTypeType");
    }

    @Override
    protected void validateScalarValue(@NotNull YAMLScalar scalar,
                                       @NotNull ProblemsHolder holder) {
        validateType(scalar, scalar.getTextValue(), holder);
    }

    public static void validateType(PsiElement element, String text, ProblemsHolder holder) {
        if (URI_PATTERN.matcher(text).find()) {
            validateUriPattern(element, text, holder);
        } else if (CURIE_PATTERN.matcher(text).find()) {
            validateCuriePattern(element, text, holder);
        } else if (PRIMITIVE_PATTERN.matcher(text).find()) {
            validatePrimitivePattern(element, text, holder);
        } else {
            holder.registerProblem(element, "Syntax error, match either: " + CURIE_PATTERN.pattern() + " or " + URI_PATTERN.pattern() + " or " + PRIMITIVE_PATTERN.pattern(), ProblemHighlightType.ERROR);
        }
    }

    private static void validateUriPattern(PsiElement element, String text, ProblemsHolder holder) {
        // create a resource to extract the namespace and localName. The resource doesn't have to exist
        // at this point, only be a valid URI pattern.
        String uri = PatternUtil.getText(text, URI_PATTERN, 1);

        Resource resource = ResourceFactory.createResource(text);
        String namespace = resource.getNameSpace();
        String localName = resource.getLocalName();
        if (namespace == null || localName == null) {
            holder.registerProblem(element,
                    "Syntax error, not a valid URI",
                    ProblemHighlightType.ERROR);
            return;
        }
        // If the namespace is already available as prefix somewhere, show a warning to convert
        // the fully qualified notation to a prefix notation
        List<String> prefixes = PrefixIndex.getPrefixes(namespace);
        if (!prefixes.isEmpty()) {
            holder.registerProblem(element, "Using fully qualified URI",
                    ProblemHighlightType.WARNING,
                    prefixes.stream()
                            .map(prefix -> getQuickFix(prefix, namespace, localName))
                            .toArray(LocalQuickFix[]::new));
        }
        // finally, validate that the resource is part of the model and a class or type
        validateResource(uri, element, holder);
    }

    private static void validateCuriePattern(PsiElement element, String text, ProblemsHolder holder) {
        if (!(element.getContainingFile() instanceof OMTFile)) {
            return;
        }
        OMTFile containingFile = (OMTFile) element.getContainingFile();
        Map<String, String> availableNamespaces = containingFile.getAvailableNamespaces(element);
        String prefix = PatternUtil.getText(text, CURIE_PATTERN, 1);
        String localName = PatternUtil.getText(text, CURIE_PATTERN, 2);
        String namespace = availableNamespaces.entrySet()
                .stream()
                .filter(stringStringEntry -> stringStringEntry.getValue().equals(prefix))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
        if (namespace == null) {
            validatePrefixReference(element, holder);
        } else {
            validateResource(namespace + localName, element, holder);
        }
    }

    private static void validatePrimitivePattern(PsiElement element, String text, ProblemsHolder holder) {
        OntClass ontClass = TTLValueParserUtil.parsePrimitiveClass(text);
        if (ontClass == null) {
            holder.registerProblem(element, "Unknown primitive type");
        }
    }

    public static void validatePrefixReference(PsiElement element, ProblemsHolder holder) {
        PsiReference[] references = element.getReferences();
        OMTParamTypePrefixReference reference = Arrays.stream(references)
                .filter(OMTParamTypePrefixReference.class::isInstance)
                .map(OMTParamTypePrefixReference.class::cast)
                .findFirst()
                .orElse(null);
        if (reference == null || reference.resolve() != null) {
            return;
        }
        YAMLPlainTextImpl plainText = reference.getElement();
        String text = plainText.getText();
        String prefix = reference.getRangeInElement().substring(text);

        LocalQuickFix[] localQuickFixes = PrefixIndex.getNamespaces(prefix).stream()
                .map(iri -> new OMTRegisterPrefixLocalQuickFix(prefix, iri))
                .toArray(LocalQuickFix[]::new);
        holder.registerProblem(
                plainText,
                UNKNOWN_PREFIX,
                ProblemHighlightType.ERROR,
                reference.getRangeInElement(),
                localQuickFixes);
    }

    public static void validateResource(String uri, PsiElement element, ProblemsHolder holder) {
        OntResource ontResource = OppModel.getInstance().getOntResource(uri, holder.getProject());
        if (ontResource == null) {
            holder.registerProblem(element, "Could not find resource <" + uri + "> in the Opp Model", ProblemHighlightType.ERROR);
        } else if (!TTLResourceUtil.isType(ontResource) && Boolean.FALSE.equals(OppModel.getInstance().isClass(ontResource))) {
            holder.registerProblem(element, "Expected a class or type", ProblemHighlightType.ERROR);
        }
    }

    /**
     * Resolves the type that is present as string
     *
     * @param type    the type part of the declaration (the part between parentheses if shorthanded, otherwise the value from the type key)
     *                examples:
     *                ont:ClassA
     *                <http://ontologyClassA>
     *                string
     * @param element the element (YAMLValue) that contains the type
     */
    public static Set<OntResource> resolveType(PsiElement element,
                                               String type) {
        return Optional.ofNullable(getQualifiedUri(element, type))
                .map(OppModel.getInstance()::toIndividuals)
                .stream()
                .flatMap(Collection::stream)
                .map(OntResource.class::cast)
                .collect(Collectors.toSet());
    }

    @Override
    public @NotNull List<? extends LookupElement> getValueLookups(@NotNull YAMLScalar insertedScalar,
                                                                  @Nullable CompletionContext completionContext) {
        // add completions for classes and types:
        PsiFile containingFile = insertedScalar.getContainingFile();
        if (containingFile instanceof OMTFile) {
            ArrayList<LookupElementBuilder> list = OppModel.getInstance().listClasses().stream()
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

    public static String getQualifiedUri(PsiElement element, String type) {
        return resolveTypeFromURI(type)
                .or(() -> resolveTypeFromCurie(element, type))
                .or(() -> resolveTypeFromPrimitive(type))
                .orElse(null);
    }

    private static Optional<String> resolveTypeFromURI(String type) {
        return PatternUtil.getTextRange(type, URI_PATTERN, 1)
                .map(textRange -> textRange.substring(type));
    }

    private static Optional<String> resolveTypeFromCurie(PsiElement element, String type) {
        final Matcher matcher = CURIE_PATTERN.matcher(type);
        if (matcher.find()) {
            String prefix = type.substring(matcher.start(1), matcher.end(1));
            String localName = type.substring(matcher.start(2), matcher.end(2));
            return Optional.ofNullable(OMTPrefixProviderUtil.resolveToFullyQualifiedUri(element, prefix, localName));
        }
        return Optional.empty();
    }

    private static Optional<String> resolveTypeFromPrimitive(String type) {
        return Optional.ofNullable(TTLValueParserUtil.parsePrimitiveClass(type))
                .map(Resource::getURI);
    }

    private static LocalQuickFix getQuickFix(String prefix, String namespace, String localName) {
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
                YAMLValue newValue = YAMLElementGenerator.getInstance(project)
                        .createYamlKeyValue("foo", prefix + ":" + localName)
                        .getValue();
                if (newValue != null) {
                    psiElement = psiElement.replace(newValue);
                }
                new OMTRegisterPrefixLocalQuickFix(prefix, namespace).addPrefix(project, psiElement);
            }
        };
    }

    @Override
    public String getFullyQualifiedURI(YAMLPlainTextImpl value) {
        return getQualifiedUri(value, value.getTextValue());
    }

}
