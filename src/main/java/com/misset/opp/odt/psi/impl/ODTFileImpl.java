package com.misset.opp.odt.psi.impl;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.util.Key;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.ODTGlobalVariable;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.builtin.commands.BuiltinCommands;
import com.misset.opp.odt.builtin.operators.BuiltinOperators;
import com.misset.opp.odt.inspection.quikfix.ODTRegisterPrefixLocalQuickFix;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.PsiRelationshipUtil;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiPrefix;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ODTFileImpl extends PsiFileBase implements ODTFile {
    private static final Key<CachedValue<Map<String, String>>> NAMESPACES = new Key<>("NAMESPACES");

    public ODTFileImpl(@NotNull FileViewProvider provider) {
        super(provider, ODTLanguage.INSTANCE);
    }

    @Override
    public @NotNull FileType getFileType() {
        return ODTFileType.INSTANCE;
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return GlobalSearchScope.fileScope(this);
    }

    @Override
    public Map<String, String> getAvailableNamespaces() {
        return CachedValuesManager.getCachedValue(this, NAMESPACES, () -> {
            final Map<String, String> namespaces = listPrefixes()
                    .stream()
                    .collect(Collectors.toMap(PsiPrefix::getNamespace, PsiPrefix::getName, (o, o2) -> o));
            return new CachedValueProvider.Result<>(namespaces, this, OntologyModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER);
        });
    }

    @Override
    public Collection<PsiPrefix> getPrefixes(String key) {
        return this.listPrefixes().stream()
                .filter(prefix -> prefix.getName().equals(key))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Variable> getVariables(String key) {
        return this.listAllVariables().stream()
                .filter(variable -> variable.getName().equals(key))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Callable> getCallables(String callId) {
        return this.listAllCallables().stream()
                .filter(callable -> callable.getCallId().equals(callId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccessible(PsiElement source, PsiElement target) {
        return PsiRelationshipUtil.canBeRelatedElement(target, source);
    }

    @Override
    public Collection<PsiCallable> listPsiCallables() {
        return PsiTreeUtil.findChildrenOfType(this, PsiCallable.class);
    }

    @Override
    public Collection<Callable> listCallables() {
        return Stream.concat(BuiltinCommands.getCommands().stream(), BuiltinOperators.getOperators().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Callable> listAllCallables() {
        return Stream.concat(listCallables().stream(), listPsiCallables().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Variable> listVariables() {
        return ODTGlobalVariable.getVariables();
    }

    @Override
    public Collection<Variable> listAllVariables() {
        return Stream.concat(listVariables().stream(), listPsiVariables().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<PsiPrefix> listPrefixes() {
        return PsiTreeUtil.findChildrenOfType(this, PsiPrefix.class);
    }

    @Override
    public Collection<PsiVariable> listPsiVariables() {
        return PsiTreeUtil.findChildrenOfType(this, ODTVariable.class)
                .stream()
                .filter(ODTVariableDelegate::isDeclaredVariable)
                .collect(Collectors.toList());
    }

    @Override
    public LocalQuickFix getRegisterPrefixQuickfix(String prefix, String namespace) {
        return new ODTRegisterPrefixLocalQuickFix(prefix, namespace);
    }

    @Override
    public boolean isStatement() {
        return false;
    }

    @Override
    public int getLineOffsetInParent() {
        return 0;
    }

    @Override
    public Collection<LocalQuickFix> getRegisterImportQuickfixes(PsiCall callable) {
        return Collections.emptyList();
    }
}
