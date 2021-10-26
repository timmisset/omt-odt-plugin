package com.misset.opp.odt.psi.impl.call;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.callable.Callable;
import com.misset.opp.callable.builtin.commands.BuiltinCommands;
import com.misset.opp.callable.builtin.operators.BuiltinOperators;
import com.misset.opp.odt.ODTMultiHostInjector;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.odt.psi.reference.ODTCallReference;
import com.misset.opp.omt.meta.providers.OMTLocalCommandProvider;
import com.misset.opp.omt.psi.impl.OMTCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Optional;

import static com.misset.opp.omt.meta.OMTMetaTreeUtil.collectLocalCommandProviders;

public abstract class ODTBaseCall extends ASTWrapperPsiElement implements PsiNamedElement {
    public ODTBaseCall(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        return new ODTCallReference(this, getCallName().getTextRangeInParent());
    }

    public abstract ODTCallName getCallName();

    /**
     * Distinct name for the call, when calling a command it will include the AT(@) symbol
     */
    public abstract String getCallId();

    public Callable getCallable() {
        return Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .map(this::getCallable)
                .or(this::getLocalCommand)
                .or(this::getBuiltin)
                .orElse(null);
    }

    @Override
    public String getName() {
        return getCallName().getText();
    }

    private Callable getCallable(PsiElement element) {
        if(element instanceof YAMLMapping) { // resolves to OMT !Activity, !Procedure etc
            return new OMTCallable((YAMLMapping) element);
        } else if(element instanceof ODTDefineName) {
            return (ODTDefineStatement)element.getParent();
        } else {
            return null;
        }
    }
    private Optional<Callable> getLocalCommand() {
        final YAMLPsiElement injectionHost = ODTMultiHostInjector.getInjectionHost(this);
        if(injectionHost == null) { return Optional.empty(); }

        final LinkedHashMap<YAMLMapping, OMTLocalCommandProvider> linkedHashMap = collectLocalCommandProviders(injectionHost);
        for(YAMLMapping mapping : linkedHashMap.keySet()) {
            OMTLocalCommandProvider callableProvider = linkedHashMap.get(mapping);
            final HashMap<String, Callable> callableMap = callableProvider.getLocalCommandsMap();
            if(callableMap.containsKey(getCallId())) {
                return Optional.of(callableMap.get(getCallId()));
            }
        }
        return Optional.empty();
    }
    private Optional<Callable> getBuiltin() {
        return Optional.ofNullable(BuiltinCommands.builtinCommands.get(getCallId()))
                .or(() -> Optional.ofNullable(BuiltinOperators.builtinOperators.get(getCallId())));
    }
}
