package com.misset.opp.odt.inspection.type;

import com.intellij.codeInspection.*;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SimpleModificationTracker;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiParserFacade;
import com.intellij.psi.impl.PsiJavaParserFacadeImpl;
import com.intellij.psi.javadoc.PsiDocComment;
import com.intellij.psi.javadoc.PsiDocTag;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTDefineParam;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTVariable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ODTCodeUntypedInspectionWarning extends LocalInspectionTool {
    /*
     * Not recognized by the PsiModificationTracker itself
     */
    public static SimpleModificationTracker PARAM_ANNOTATION = new SimpleModificationTracker();

    protected static final String WARNING = "Annotate parameter with type";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Shows a warning when a parameter can be typed.\n" +
                "Parameters can be typed by adding a JavaDoc to the DEFINE statement.";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder, boolean isOnTheFly, @NotNull LocalInspectionToolSession session) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTVariable) {
                    ODTVariable variable = (ODTVariable) element;
                    if (variable.canBeAnnotated() && variable.getType().isEmpty()) {
                        holder.registerProblem(
                                element,
                                WARNING,
                                getAnnotationBlockQuickFix()
                        );
                    }
                }
            }
        };
    }

    private LocalQuickFix getAnnotationBlockQuickFix() {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Annotate";
            }

            @Override
            public @IntentionName @NotNull String getName() {
                return WARNING;
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                ODTElementGenerator elementGenerator = ODTElementGenerator.getInstance(project);

                ODTVariable variable = (ODTVariable) descriptor.getPsiElement();
                ODTScriptLine defineStatementLine = PsiTreeUtil.getParentOfType(descriptor.getPsiElement(), ODTScriptLine.class);
                if (defineStatementLine == null) {
                    return;
                }
                PsiElement firstChild = defineStatementLine.getFirstChild();
                if (firstChild == null) {
                    return;
                }

                ODTDefineParam defineParam = PsiTreeUtil.findChildOfType(defineStatementLine, ODTDefineParam.class);
                if (defineParam == null) {
                    // nothing to annotate
                    return;
                }

                List<String> description = Collections.emptyList();
                List<PsiDocTag> tags = new ArrayList<>();
                if (firstChild instanceof PsiDocComment) {
                    PsiDocComment psiDocComment = (PsiDocComment) firstChild;
                    tags.addAll(List.of(psiDocComment.getTags()));
                    description = Arrays.stream((psiDocComment).getDescriptionElements())
                            .map(PsiElement::getText)
                            .map(String::trim)
                            .filter(s -> !s.isBlank())
                            .collect(Collectors.toList());
                }
                tags.add(new PsiJavaParserFacadeImpl(project).createDocTagFromText(
                        "@param " + variable.getName() + " (TypeOrClass)"
                ));
                // order so that the parameter annotations are sorted by the order in which
                // they appear in the DefineParam block
                tags.sort(Comparator.comparing(o -> getTagIndex(o, defineParam)));

                // create a new JavaDoc:
                PsiDocComment javaDocs = elementGenerator.createJavaDocs(description, tags);

                // either replace or add
                if (firstChild instanceof PsiDocComment) {
                    firstChild.replace(javaDocs);
                } else {
                    PsiElement newComment = defineStatementLine.addBefore(javaDocs, firstChild);
                    defineStatementLine.addAfter(
                            PsiParserFacade.SERVICE.getInstance(project).createWhiteSpaceFromText("\n"),
                            newComment);
                }

                PARAM_ANNOTATION.incModificationCount();
            }

            private Integer getTagIndex(PsiDocTag tag, ODTDefineParam defineParam) {
                String paramName = tag.getValueElement() != null ? tag.getValueElement().getText() : "";
                List<ODTVariable> variableList = defineParam.getVariableList();
                for (int i = 0; i < variableList.size(); i++) {
                    String name = variableList.get(i).getName();
                    if (name != null && name.equals(paramName)) {
                        return i;
                    }
                }
                return -1;
            }
        };
    }
}
