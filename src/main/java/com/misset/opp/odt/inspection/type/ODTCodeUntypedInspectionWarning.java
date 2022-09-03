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
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.querystep.ODTResolvableVariableStep;
import com.misset.opp.resolvable.Callable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class ODTCodeUntypedInspectionWarning extends LocalInspectionTool {
    /*
     * Not recognized by the PsiModificationTracker itself
     */
    public static final SimpleModificationTracker PARAM_ANNOTATION = new SimpleModificationTracker();

    protected static final String ANNOTATE_PARAMETER_WITH_TYPE = "Annotate parameter with type";
    protected static final String ANNOTATE_BASE_WITH_TYPE = "Annotate base with type";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Shows a warning when a parameter or base can be typed.\n" +
                "Parameters can be typed by adding a JavaDoc to the DEFINE statement.";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly,
                                                   @NotNull LocalInspectionToolSession session) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTVariable) {
                    visitVariable(element, holder);
                } else if (element instanceof ODTDefineQueryStatement) {
                    visitDefinedQueryStatement((ODTDefineQueryStatement) element, holder);
                }
            }
        };
    }

    private void visitVariable(@NotNull PsiElement element, @NotNull ProblemsHolder holder) {
        ODTVariable variable = (ODTVariable) element;
        if (variable.canBeAnnotated() && variable.resolve().isEmpty()) {
            holder.registerProblem(
                    element,
                    ANNOTATE_PARAMETER_WITH_TYPE,
                    getParamAnnotationBlockQuickFix()
            );
        }
    }

    private void visitDefinedQueryStatement(ODTDefineQueryStatement element, @NotNull ProblemsHolder holder) {
        ODTQuery query = element.getQuery();
        Set<OntResource> previousStep = query.resolvePreviousStep();
        if (!previousStep.isEmpty()) {
            return;
        }

        if (!(query instanceof ODTQueryPath)) {
            return;
        }
        ODTQueryPath queryPath = (ODTQueryPath) query;
        ODTQueryOperationStep odtQueryOperationStep = queryPath.getQueryOperationStepList().get(0);
        ODTQueryStep queryStep = odtQueryOperationStep.getQueryStep();
        if (queryStep != null &&
                queryStep.resolvePreviousStep().isEmpty() &&
                query.resolve().isEmpty() &&
                PsiTreeUtil.getDeepestFirst(queryStep).equals(PsiTreeUtil.getDeepestFirst(query)) &&
                canBeBaseAnnotated(queryStep)) {
            // a @base annotation is applicable
            holder.registerProblem(
                    element.getDefineName(),
                    ANNOTATE_BASE_WITH_TYPE,
                    getBaseAnnotationBlockQuickFix()
            );
        }
    }

    private boolean canBeBaseAnnotated(ODTResolvable queryStep) {
        if (queryStep instanceof ODTResolvableVariableStep) {
            return false;
        }
        if (queryStep instanceof ODTOperatorCall) {
            ODTOperatorCall operatorCall = (ODTOperatorCall) queryStep;
            Callable callable = operatorCall.getCallable();
            return callable != null && !callable.isStatic();
        }
        return true;
    }

    private LocalQuickFix getBaseAnnotationBlockQuickFix() {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Annotate";
            }

            @Override
            public @IntentionName @NotNull String getName() {
                return ANNOTATE_BASE_WITH_TYPE;
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                addAnnotation(project, descriptor, "base", null);
            }
        };
    }

    private LocalQuickFix getParamAnnotationBlockQuickFix() {
        return new LocalQuickFix() {
            @Override
            public @IntentionFamilyName @NotNull String getFamilyName() {
                return "Annotate";
            }

            @Override
            public @IntentionName @NotNull String getName() {
                return ANNOTATE_PARAMETER_WITH_TYPE;
            }

            @Override
            public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                ODTVariable variable = (ODTVariable) descriptor.getPsiElement();
                addAnnotation(project, descriptor, "param", variable.getName());
            }

        };
    }

    private void addAnnotation(@NotNull Project project,
                               @NotNull ProblemDescriptor descriptor,
                               @NotNull String type,
                               @Nullable String name) {

        ODTScriptLine defineStatementLine = PsiTreeUtil.getParentOfType(descriptor.getPsiElement(), ODTScriptLine.class);
        if (defineStatementLine == null) {
            return;
        }
        PsiElement firstChild = defineStatementLine.getFirstChild();
        if (firstChild == null) {
            return;
        }

        ODTDefineParam defineParam = PsiTreeUtil.findChildOfType(defineStatementLine, ODTDefineParam.class);

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
                String.format("@%s%s(TypeOrClass)", type, (name != null ? " " + name + " " : " "))
        ));
        // order so that the parameter annotations are sorted by the order in which
        // they appear in the DefineParam block
        tags.sort(Comparator.comparing(o -> getTagIndex(o, defineParam)));

        // create a new JavaDoc:
        ODTElementGenerator elementGenerator = ODTElementGenerator.getInstance(project);
        PsiDocComment javaDocs = elementGenerator.createJavaDocs(description, tags);

        // either replace or add
        if (firstChild instanceof PsiDocComment) {
            firstChild.replace(javaDocs);
        } else {
            PsiElement newComment = defineStatementLine.addBefore(javaDocs, firstChild);
            defineStatementLine.addAfter(
                    PsiParserFacade.getInstance(project).createWhiteSpaceFromText("\n"),
                    newComment);
        }
        PARAM_ANNOTATION.incModificationCount();
    }

    private Integer getTagIndex(PsiDocTag tag, ODTDefineParam defineParam) {
        if (tag.getName().equals("@base")) {
            return -1;
        }
        if (defineParam == null) {
            return 0;
        }
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
}
