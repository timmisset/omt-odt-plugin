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
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.resolvable.querystep.traverse.ODTResolvableCurieElementStep;
import com.misset.opp.odt.psi.impl.resolvable.querystep.traverse.ODTResolvableIriStep;
import com.misset.opp.omt.indexing.OMTPrefixIndex;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ODTQualifiedURIInspection extends LocalInspectionTool {

    protected static final String WEAK_WARNING = "Using fully qualified URI";

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
                if (element instanceof ODTResolvableIriStep) {
                    String namespace = ((ODTResolvableIriStep) element).getNamespace();
                    String localName = ((ODTResolvableIriStep) element).getLocalName();
                    List<String> prefixes = OMTPrefixIndex.getPrefixes(namespace);
                    if (!prefixes.isEmpty()) {
                        holder.registerProblem(element, WEAK_WARNING,
                                prefixes.stream()
                                        .map(prefix -> getQuickFix(prefix, namespace, localName))
                                        .toArray(LocalQuickFix[]::new));
                    }
                }
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
                        ODTFile containingFile = (ODTFile) psiElement.getContainingFile();

                        // replace the current element:
                        ODTResolvableCurieElementStep curie = ODTElementGenerator.getInstance(project)
                                .fromFile(prefix + ":" + localName, ODTResolvableCurieElementStep.class);
                        if (curie != null) {
                            PsiElement replacement = psiElement.replace(curie);
                            ProblemDescriptorImpl newProblemDescriptor = new ProblemDescriptorImpl(
                                    replacement,
                                    replacement,
                                    descriptor.getDescriptionTemplate(),
                                    new LocalQuickFix[0],
                                    descriptor.getHighlightType(),
                                    descriptor.isAfterEndOfLine(),
                                    TextRange.allOf(curie.getText()),
                                    false);
                            containingFile.getRegisterPrefixQuickfix(prefix, namespace).applyFix(project, newProblemDescriptor);
                        }
                    }
                };
            }
        };
    }
}
