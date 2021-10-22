package com.misset.opp.odt.psi.impl.variables.delegates;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.model.scalars.OMTVariableNameType;
import com.misset.opp.omt.meta.model.variables.OMTVariableType;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;

public abstract class ODTBaseVariableDelegate implements ODTVariableDelegate {

    protected final ODTVariable element;
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
        if(injectionHost != null) {
            final YamlMetaTypeProvider.MetaTypeProxy metaTypeProxy = new OMTMetaTypeProvider().getMetaTypeProxy(
                    injectionHost);
            if(metaTypeProxy == null) { return false; }
            final YamlMetaType metaType = metaTypeProxy.getMetaType();
            return metaType instanceof OMTVariableNameType ||
                    metaType instanceof OMTVariableType;
        }
        return false;
    }

}
