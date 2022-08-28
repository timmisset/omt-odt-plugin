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
import com.misset.opp.odt.ODTFileType;
import com.misset.opp.odt.ODTLanguage;
import com.misset.opp.odt.builtin.commands.BuiltinCommands;
import com.misset.opp.odt.builtin.operators.BuiltinOperators;
import com.misset.opp.odt.inspection.quikfix.ODTRegisterPrefixLocalQuickFix;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.impl.variable.delegate.ODTVariableDelegate;
import com.misset.opp.odt.psi.util.PsiRelationshipUtil;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.global.GlobalVariable;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiPrefix;
import com.misset.opp.resolvable.psi.PsiVariable;
import com.misset.opp.ttl.model.OppModel;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ODTFileImpl extends PsiFileBase implements ODTFile {
    private static final Key<CachedValue<Map<String, String>>> NAMESPACES = new Key<>("NAMESPACES");

    private final HashMap<String, Collection<PsiPrefix>> prefixes = new HashMap<>();
    private final HashMap<String, Collection<Variable>> allVariables = new HashMap<>();
    private final HashMap<String, Collection<Callable>> allCallables = new HashMap<>();

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
            return new CachedValueProvider.Result<>(namespaces, this, OppModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER);
        });
    }

    @Override
    public Collection<PsiPrefix> getPrefixes(String key) {
        return Optional.ofNullable(
                withCache(prefixes, this::listPrefixes, PsiPrefix::getName).get(key)
        ).orElse(new ArrayList<>());
    }

    @Override
    public Collection<Variable> getVariables(String key) {
        return Optional.ofNullable(
                withCache(allVariables, this::listAllVariables, Variable::getName).get(key)
        ).orElse(new ArrayList<>());
    }

    @Override
    public Collection<Callable> getCallables(String callId) {
        return Optional.ofNullable(
                withCache(allCallables, this::listAllCallables, Callable::getCallId).get(callId)
        ).orElse(new ArrayList<>());
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
        return GlobalVariable.getVariables();
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

    private <T> HashMap<String, Collection<T>> withCache(
            HashMap<String, Collection<T>> cache,
            Supplier<Collection<T>> ifEmpty,
            Function<T, String> getKey
    ) {
        if (cache.isEmpty()) {
            // recalculate
            cache.putAll(ifEmpty.get().stream().collect(Collectors.groupingBy(getKey)));
        }
        return cache;
    }

    @Override
    public LocalQuickFix getRegisterPrefixQuickfix(String prefix, String namespace) {
        return new ODTRegisterPrefixLocalQuickFix(prefix, namespace);
    }

    @Override
    public Collection<LocalQuickFix> getRegisterImportQuickfixes(PsiCall callable) {
        return Collections.emptyList();
    }
}
