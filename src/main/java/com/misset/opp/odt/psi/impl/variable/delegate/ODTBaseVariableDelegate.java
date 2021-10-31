package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTDefineQueryStatement;
import com.misset.opp.odt.psi.ODTScript;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.ODTVariableAssignment;
import com.misset.opp.odt.psi.ODTVariableValue;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameMetaType;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.meta.model.variables.OMTVariableMetaType;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;

public abstract class ODTBaseVariableDelegate implements ODTVariableDelegate {

    protected final ODTVariable element;
    protected static final Key<CachedValue<Boolean>> IS_DECLARED_VARIABLE = new Key<>("IS_DECLARED_VARIABLE");

    public ODTBaseVariableDelegate(ODTVariable element) {
        this.element = element;
    }

    /**
     * Checks if the current ODTVariable is placed in a known OMTVariableProvider location
     * When true, the variable should never return a reference and instead should be included in the findUsages
     */
    public boolean isOMTVariableProvider() {
        final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(element.getProject());
        final PsiLanguageInjectionHost injectionHost = instance.getInjectionHost(element.getContainingFile());
        if (injectionHost instanceof YAMLValue) {
            final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy =
                    OMTMetaTypeProvider.getInstance(element.getProject()).getValueMetaType((YAMLValue) injectionHost);
            if (metaTypeProxy == null) {
                return false;
            }
            final YamlMetaType metaType = metaTypeProxy.getMetaType();
            return metaType instanceof OMTVariableNameMetaType ||
                    metaType instanceof OMTVariableMetaType ||
                    metaType instanceof OMTParamMetaType;
        }
        return false;
    }

    protected boolean isAssignmentPart() {
        return PsiTreeUtil.getParentOfType(element, ODTVariableAssignment.class, ODTVariableValue.class) instanceof ODTVariableValue;
    }

    @Override
    public boolean canBeDeclaredVariable(ODTVariable variable) {
        return element.isDeclaredVariable() && element.sameNameAs(variable) && isAccessibleTo(variable);
    }

    private boolean isAccessibleTo(ODTVariable variableWrapper) {
        if(variableWrapper == null || !variableWrapper.isValid() || !element.isValid()) { return false; }
        final PsiElement commonParent = PsiTreeUtil.findCommonParent(element, variableWrapper);
        if (commonParent instanceof ODTScript) {
            // a script block is the common parent, this means the variable is accessible only if this script is also
            // the first Script parent of the declared variable, i.e.
            // VAR $x
            // IF ... { $x } <-- $x has access to VAR $x
            //
            // {
            //    { VAR $x; }
            //    $x <-- $x has no access to VAR $x
            // }
            return PsiTreeUtil.getParentOfType(element, ODTScript.class) == commonParent &&
                    element.getTextOffset() < variableWrapper.getTextOffset();
        }
        return commonParent instanceof ODTDefineQueryStatement;
    }
}
