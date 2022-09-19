package com.misset.opp.odt.inspection;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.ProblemDescriptorImpl;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.intellij.psi.javadoc.PsiDocTag;
import com.misset.opp.indexing.PrefixIndex;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTCurieElement;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTIriStep;
import com.misset.opp.util.UriPatternUtil;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.jena.ontology.OntClass;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ODTQualifiedURIInspection extends LocalInspectionTool {

    protected static final String USING_FULLY_QUALIFIED_URI = "Using fully qualified URI";

    @Override
    public @Nullable
    @Nls String getStaticDescription() {
        return "Inspect fully qualified URI usages and suggests to register a prefix based on the namespace to prefix" +
                "mapping in the rest of the project.";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {

        return new PsiElementVisitor() {

            @Override
            public void visitElement(@NotNull PsiElement element) {
                MutablePair<String, String> pair = new MutablePair<>(null, null);
                if (element instanceof ODTIriStep) {
                    pair.setLeft(((ODTIriStep) element).getNamespace());
                    pair.setRight(((ODTIriStep) element).getLocalName());
                } else if (element instanceof PsiDocTag && Set.of("param", "base").contains(((PsiDocTag) element).getName())) {
                    // extract from DocTag
                    element = visitDocTag((PsiDocTag) element, pair);
                }

                if (pair.getLeft() != null && pair.getRight() != null && element.getContainingFile() instanceof ODTFile) {
                    ODTFile containingFile = (ODTFile) element.getContainingFile();
                    Map<String, String> availableNamespaces = containingFile.getAvailableNamespaces();
                    if (availableNamespaces.containsKey(pair.getLeft())) {
                        holder.registerProblem(element, USING_FULLY_QUALIFIED_URI, getRewriteToCurieQuickFix(availableNamespaces.get(pair.getLeft()), pair.getRight()));
                    } else {
                        tryRegisterNewPrefix(element, pair, holder);
                    }
                }
            }
        };
    }

    private void tryRegisterNewPrefix(@NotNull PsiElement element,
                                      MutablePair<String, String> pair,
                                      ProblemsHolder holder) {
        List<String> prefixes = PrefixIndex.getPrefixes(pair.getLeft());
        if (!prefixes.isEmpty()) {
            holder.registerProblem(element, USING_FULLY_QUALIFIED_URI,
                    prefixes.stream()
                            .map(prefix -> getRegisterQuickFix(prefix, pair.getLeft(), pair.getRight()))
                            .toArray(LocalQuickFix[]::new));
        }
    }

    private PsiElement visitDocTag(PsiDocTag docTag, MutablePair<String, String> pair) {
        int position = docTag.getName().equals("param") ? 1 : 0;
        if (docTag.getDataElements().length > position) {
            PsiElement dataElement = docTag.getDataElements()[position];
            String uri = dataElement.getText();
            if (UriPatternUtil.isUri(uri)) {
                String namespace = UriPatternUtil.getNamespace(uri);
                String localName = UriPatternUtil.getLocalname(uri);
                OntClass ontClass = OntologyModel.getInstance().getClass(namespace + localName);
                if (ontClass != null) {
                    pair.setLeft(namespace);
                    pair.setRight(localName);
                    return dataElement;
                }
            }
            return docTag;
        }
        return null;
    }

    private LocalQuickFix getRegisterQuickFix(String prefix, String namespace, String localName) {
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
                PsiFile containingFile = descriptor.getPsiElement().getContainingFile();
                if (containingFile instanceof ODTFile) {
                    ODTFile odtFile = (ODTFile) containingFile;

                    // replace the current element:
                    PsiElement replacement = replaceWithCurie(prefix, localName, descriptor.getPsiElement());
                    ProblemDescriptorImpl newProblemDescriptor = new ProblemDescriptorImpl(
                            replacement,
                            replacement,
                            descriptor.getDescriptionTemplate(),
                            new LocalQuickFix[0],
                            descriptor.getHighlightType(),
                            descriptor.isAfterEndOfLine(),
                            TextRange.allOf(replacement.getText()),
                            false);
                    odtFile.getRegisterPrefixQuickfix(prefix, namespace).applyFix(project, newProblemDescriptor);
                }
            }
        };
    }

    private LocalQuickFix getRewriteToCurieQuickFix(String prefix, String localName) {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Use prefix";
            }

            @Override
            public @IntentionName @NotNull String getName() {
                return "Rewrite to " + prefix + ":" + localName;
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                replaceWithCurie(prefix, localName, descriptor.getPsiElement());
            }
        };
    }

    private PsiElement replaceWithCurie(String prefix, String localName, PsiElement element) {
        ODTElementGenerator generator = ODTElementGenerator.getInstance(element.getProject());
        if (element instanceof ODTIriStep) {
            // for regular usages in the ODT language
            ODTCurieElement curie = generator.fromFile(prefix + ":" + localName, ODTCurieElement.class);
            return element.replace(curie);
        } else {
            // specifically for JavaDoc data containers:
            PsiDocTag psiDocTag = generator.fromFile("/**\n" +
                    " * @base (" + prefix + ":" + localName + ")\n" +
                    "*/\n" +
                    "DEFINE QUERY query => true;", PsiDocTag.class);
            return element.replace(psiDocTag.getDataElements()[0]);
        }
    }
}
