package com.misset.opp.odt.inspection.resolvable;

import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.odt.psi.ODTQueryStatement;
import com.misset.opp.odt.psi.ODTResolvableValue;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.omt.injection.OMTODTInjectionUtil;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.resolvable.psi.PsiResolvable;
import com.misset.opp.ttl.model.OppModelConstants;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.YamlMetaType;

import java.util.Set;

import static com.intellij.codeInspection.ProblemHighlightType.WEAK_WARNING;

public class ODTIgnoredReturnValueInspection extends LocalInspectionTool {

    protected static final String RESULT_IS_IGNORED = "Result is ignored";

    @Override
    public @Nullable @Nls String getStaticDescription() {
        return "Inspect if returned values from statements are used";
    }

    @Override
    public @NotNull PsiElementVisitor buildVisitor(@NotNull ProblemsHolder holder,
                                                   boolean isOnTheFly) {
        return new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                if (element instanceof ODTCommandCall) {
                    validateCall((ODTCommandCall) element, holder);
                }
                if (element instanceof ODTQueryStatement) {
                    validateQueryStatement((ODTQueryStatement) element, holder);
                }
            }
        };
    }

    private void validateCall(ODTCommandCall call, @NotNull ProblemsHolder holder) {
        if (call.getCallable().isBuiltin()) {
            return;
        }
        validateReturnValue(call, holder);
    }

    private void validateQueryStatement(@NotNull ODTQueryStatement queryStatement, @NotNull ProblemsHolder holder) {
        PsiElement parent = queryStatement.getParent();
        if (parent instanceof ODTVariableValue || parent instanceof ODTResolvableValue) {
            // when part of a ResolvableValue or VariableValue, it is either used as the value part in an
            // assignment or a call signature argument. In any case, it's being used.
            return;
        }

        final YamlMetaType injectionMetaType = OMTODTInjectionUtil.getInjectionMetaType(queryStatement);
        if (injectionMetaType != null && !(injectionMetaType instanceof OMTScriptMetaType)) {
            return;
        }
        validateReturnValue(queryStatement.getQuery(), holder);
    }

    private void validateReturnValue(PsiResolvable resolvable, @NotNull ProblemsHolder holder) {
        Set<OntResource> resolved = resolvable.resolve();
        if (!resolved.isEmpty() && !resolved.contains(OppModelConstants.VOID)) {
            // something is being returned, show warning:
            holder.registerProblem(resolvable, RESULT_IS_IGNORED, WEAK_WARNING);
        }
    }
}
