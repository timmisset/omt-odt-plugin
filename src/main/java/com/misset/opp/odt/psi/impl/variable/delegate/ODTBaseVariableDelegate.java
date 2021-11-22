package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.meta.model.variables.OMTVariableMetaType;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

public abstract class ODTBaseVariableDelegate implements ODTVariableDelegate {
    private static final Key<CachedValue<Boolean>> IS_OMT_VARIABLE_PROVIDER = new Key<>("IS_OMT_VARIABLE_PROVIDER");
    protected final ODTVariable element;

    public ODTBaseVariableDelegate(ODTVariable element) {
        this.element = element;
    }

    /**
     * Checks if the current ODTVariable is placed in a known OMTVariableProvider location
     * When true, the variable should never return a reference and instead should be included in the findUsages
     */
    public boolean isOMTVariableProvider() {
        return CachedValuesManager.getCachedValue(element, IS_OMT_VARIABLE_PROVIDER, () -> {
            final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(element.getProject());
            final PsiLanguageInjectionHost injectionHost = instance.getInjectionHost(element.getContainingFile());
            if (injectionHost instanceof YAMLValue) {
                final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy =
                        OMTMetaTypeProvider.getInstance(element.getProject())
                                .getValueMetaType((YAMLValue) injectionHost);
                if (metaTypeProxy == null) {
                    return new CachedValueProvider.Result<>(false, PsiModificationTracker.MODIFICATION_COUNT);
                }
                final YamlMetaType metaType = metaTypeProxy.getMetaType();
                return new CachedValueProvider.Result<>(metaType instanceof OMTVariableNameMetaType ||
                        metaType instanceof OMTVariableMetaType ||
                        metaType instanceof OMTParamMetaType, PsiModificationTracker.MODIFICATION_COUNT);
            }
            return new CachedValueProvider.Result<>(false, PsiModificationTracker.MODIFICATION_COUNT);
        });
    }

    protected boolean isAssignmentPart() {
        return PsiTreeUtil.getParentOfType(element,
                ODTVariableAssignment.class,
                ODTVariableValue.class) instanceof ODTVariableValue;
    }

    @Override
    public boolean canBeDeclaredVariable(ODTVariable variable) {
        return element != variable &&
                element.sameNameAs(variable) &&
                element.isDeclaredVariable() &&
                validateCommonParent(variable);
    }

    private boolean validateCommonParent(ODTVariable usage) {
        final PsiElement commonParent = PsiTreeUtil.findCommonParent(element, usage);
        // DEFINE COMMAND command($variable) => { $variable } <-- same common parent as ODTDefineStatement OK
        // DEFINE COMMAND command => { VAR $variable; @LOG($variable); } <-- common parent will be script, so not OK (yet)
        if (commonParent instanceof ODTDefineStatement) {
            return true;
        }

        if (commonParent instanceof ODTScript) {
            // only when the declared variable is in the root of the common parent:
            // VAR $variable
            // IF true { @LOG($variable); } <-- passed

            // IF true { VAR $variable; } ELSE { @LOG($variable); } <-- failed
            return PsiTreeUtil.getParentOfType(element, ODTDefineStatement.class, ODTScript.class) == commonParent;
        }
        return false;
    }
}
