package com.misset.opp.odt.psi.reference;

import com.intellij.codeInsight.daemon.EmptyResolveMessageProvider;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.LocalQuickFixProvider;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.util.InspectionMessage;
import com.intellij.codeInspection.util.IntentionFamilyName;
import com.intellij.codeInspection.util.IntentionName;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTScriptLine;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.omt.meta.providers.OMTVariableProvider;
import com.misset.opp.util.LoggerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class ODTVariableReference extends ODTPolyReferenceBase<ODTVariable> implements LocalQuickFixProvider, EmptyResolveMessageProvider {
    private static final Key<CachedValue<ResolveResult[]>> RESULT = new Key<>("RESULT");
    private static final Key<ODTVariableReference> REFERENCE = new Key<>("REFERENCE");
    Logger LOGGER = Logger.getInstance(ODTVariableReference.class);

    public static ODTVariableReference forVariable(ODTVariable variable) {
        if (!REFERENCE.isIn(variable)) {
            ODTVariableReference odtVariableReference = new ODTVariableReference(variable);
            REFERENCE.set(variable, odtVariableReference);
            return odtVariableReference;
        }
        return REFERENCE.get(variable);
    }

    private ODTVariableReference(@NotNull ODTVariable element) {
        super(element, TextRange.allOf(element.getText()), false);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        return LoggerUtil.computeWithLogger(LOGGER,
                "Resolving ODTVariableReference for " + getElement().getName(),
                this::getCachedResultOrResolve);
    }

    private ResolveResult @NotNull [] getCachedResultOrResolve() {
        return CachedValuesManager.getCachedValue(getElement(), RESULT, () -> {
            ResolveResult[] calculate = calculate();
            return getElement().getODTFile().getCachedValue(calculate);
        });
    }

    private ResolveResult @NotNull [] calculate() {
        if (!getElement().isValid()) {
            return ResolveResult.EMPTY_ARRAY;
        }
        return resolveInODT()
                .or(this::resolveFromProvider)
                .orElse(ResolveResult.EMPTY_ARRAY);
    }

    private Optional<ResolveResult[]> resolveFromProvider() {
        return getElement().getODTFile()
                .resolveInOMT(OMTVariableProvider.class,
                        OMTVariableProvider.KEY,
                        getElement().getName(),
                        OMTVariableProvider::getVariableMap)
                .map(this::toResults);
    }

    private Optional<ResolveResult[]> resolveInODT() {
        final ODTScript script = PsiTreeUtil.getTopmostParentOfType(getElement(), ODTScript.class);
        if (script == null) {
            return Optional.empty();
        }

        return PsiTreeUtil.findChildrenOfType(script, ODTVariable.class)
                .stream()
                .filter(variable -> variable.canBeDeclaredVariable(getElement()))
                .min((o1, o2) -> Integer.compare(o1.getTextOffset(), o2.getTextOffset()) * -1)
                .map(PsiElementResolveResult::createResults);
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return getElement().setName(newElementName);
    }

    // override required to allow caching mechanism
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ODTVariableReference &&
                ((ODTVariableReference) obj).getElement().equals(getElement());
    }


    @Override
    public @InspectionMessage @NotNull String getUnresolvedMessagePattern() {
        return "Cannot resolve variable '" + getElement().getName() + "'";
    }

    @Override
    public LocalQuickFix @Nullable [] getQuickFixes() {
        return new LocalQuickFix[]{
                new LocalQuickFix() {
                    @Override
                    public @IntentionFamilyName @NotNull String getFamilyName() {
                        return "Declare variable";
                    }

                    @Override
                    public @IntentionName @NotNull String getName() {
                        return "Declare variable";
                    }

                    @Override
                    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
                        PsiElement psiElement = descriptor.getPsiElement();
                        ODTScriptLine scriptLine = PsiTreeUtil.getTopmostParentOfType(psiElement, ODTScriptLine.class);
                        if (scriptLine == null) {
                            return;
                        }
                        ODTScript script = (ODTScript) scriptLine.getParent();
                        if (script == null) {
                            return;
                        }

                        ODTScriptLine variableDeclaration = ODTElementGenerator.getInstance(project).createVariableDeclaration(getElement().getName());
                        script.addBefore(variableDeclaration, scriptLine);
                    }
                }
        };
    }
}
