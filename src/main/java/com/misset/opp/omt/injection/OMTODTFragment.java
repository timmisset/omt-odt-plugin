package com.misset.opp.omt.injection;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.search.SearchScope;
import com.misset.opp.exception.OMTODTPluginException;
import com.misset.opp.odt.psi.ODTFile;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import com.misset.opp.omt.inspection.quickfix.OMTRegisterPrefixLocalQuickFix;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.providers.*;
import com.misset.opp.omt.meta.scalars.scripts.OMTScriptMetaType;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.util.OMTImportUtil;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Variable;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiPrefix;
import com.misset.opp.resolvable.psi.PsiVariable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;
import org.jetbrains.yaml.psi.impl.YAMLScalarListImpl;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OMTODTFragment extends ODTFileImpl implements ODTFile {
    public OMTODTFragment(@NotNull FileViewProvider provider) {
        super(provider);
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> {
            Object key = keyExtractor.apply(t);
            if (key != null) {
                return seen.putIfAbsent(key, Boolean.TRUE) == null;
            } else {
                return false;
            }
        };
    }

    @Override
    public boolean isAccessible(PsiElement source, PsiElement target) {
        return target.getContainingFile() != this || super.isAccessible(source, target);
    }

    @Override
    public Collection<PsiCallable> listPsiCallables() {
        return joinedCollection(super.listPsiCallables(), getPsiCallables().values());
    }

    @Override
    public Collection<Callable> listCallables() {
        return Stream.concat(super.listCallables().stream(), getNonPsiCallables().values().stream().flatMap(Collection::stream))
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<PsiPrefix> listPrefixes() {
        return joinedCollection(super.listPrefixes(), getPrefixes().values());
    }

    @Override
    public Collection<PsiVariable> listPsiVariables() {
        return joinedCollection(super.listPsiVariables(), getPsiVariables().values());
    }

    @Override
    public Collection<Variable> listVariables() {
        return Stream.concat(super.listVariables().stream(), getNonPsiVariables().values().stream().flatMap(Collection::stream))
                .distinct()
                .collect(Collectors.toList());
    }

    private synchronized <T extends PsiElement> Collection<T> joinedCollection(Collection<T> collectionA,
                                                                               Collection<Collection<T>> collectionB) {
        return Stream.concat(collectionA.stream(), collectionB.stream().flatMap(Collection::stream))
                .filter(distinctByKey(PsiElement::getOriginalElement))
                .collect(Collectors.toList());
    }

    private synchronized <T extends PsiElement> void addToCollectionMap(Map<String, Collection<T>> collectionHashMap,
                                                                        Map<String, Collection<T>> newItems) {
        for (Map.Entry<String, Collection<T>> entry : newItems.entrySet()) {
            collectionHashMap.computeIfAbsent(entry.getKey(), ignored -> new ArrayList<>())
                    .addAll(entry.getValue());
        }
    }

    private OMTFile getHostFile() {
        PsiFile psiFile = InjectedLanguageManager.getInstance(getProject()).getTopLevelFile(this);
        if (!(psiFile instanceof OMTFile)) {
            throw new OMTODTPluginException("Expected ODT fragment to be hosted in OMT file");
        }
        return (OMTFile) psiFile;
    }

    private PsiLanguageInjectionHost getHost() {
        @Nullable PsiLanguageInjectionHost injectionHost = InjectedLanguageManager.getInstance(getProject()).getInjectionHost(this);
        if (injectionHost == null) {
            throw new OMTODTPluginException("Expected ODT fragment to be hosted in OMT host");
        }
        return injectionHost;
    }

    @Override
    public @NotNull SearchScope getUseScope() {
        return getHostFile().getUseScope();
    }

    @Override
    public LocalQuickFix getRegisterPrefixQuickfix(String prefix, String namespace) {
        return new OMTRegisterPrefixLocalQuickFix(prefix, namespace);
    }

    @Override
    public Collection<LocalQuickFix> getRegisterImportQuickfixes(PsiCall call) {
        return OMTImportUtil.getImportQuickFixes(getHostFile(), call);
    }

    @Override
    public boolean isStatement() {
        final YamlMetaType injectionMetaType = OMTODTInjectionUtil.getInjectionMetaType(this);
        return injectionMetaType != null && !(injectionMetaType instanceof OMTScriptMetaType);
    }

    private HashMap<String, Collection<PsiVariable>> getPsiVariables() {
        HashMap<String, Collection<PsiVariable>> psiVariables = new HashMap<>();
        Map<YAMLMapping, OMTVariableProvider> variableProviders =
                OMTMetaTreeUtil.collectMetaParents(getHost(), YAMLMapping.class, OMTVariableProvider.class);

        variableProviders.entrySet().stream()
                .map(entry -> entry.getValue().getVariableMap(entry.getKey()))
                .forEach(map -> addToCollectionMap(psiVariables, map));
        return psiVariables;
    }

    private HashMap<String, Collection<? extends Variable>> getNonPsiVariables() {
        HashMap<String, Collection<? extends Variable>> nonPsiVariables = new HashMap<>();

        Map<YAMLPsiElement, OMTLocalVariableProvider> localVariableProviders =
                OMTMetaTreeUtil.collectMetaParents(getHost(), YAMLPsiElement.class, OMTLocalVariableProvider.class);

        localVariableProviders.entrySet().stream()
                .map(entry -> entry.getValue().getLocalVariableMap(entry.getKey()))
                .forEach(map -> map.forEach((key, variable) -> nonPsiVariables.put(key, Collections.singleton(variable))));

        return nonPsiVariables;
    }

    private HashMap<String, Collection<PsiCallable>> getPsiCallables() {
        HashMap<String, Collection<PsiCallable>> psiCallables = new HashMap<>();
        // PsiCallables provided by the normal Psi tree
        Map<YAMLMapping, OMTCallableProvider> callableProviders =
                OMTMetaTreeUtil.collectMetaParents(getHost(), YAMLMapping.class, OMTCallableProvider.class);

        callableProviders.entrySet().stream()
                .map(entry -> entry.getValue().getCallableMap(entry.getKey(), getHost()))
                .forEach(map -> addToCollectionMap(psiCallables, map));
        return psiCallables;
    }

    private HashMap<String, Collection<? extends Callable>> getNonPsiCallables() {
        HashMap<String, Collection<? extends Callable>> nonPsiCallables = new HashMap<>();

        // Callables that are available based on the fragments position in the OMT file:
        Map<YAMLPsiElement, OMTLocalCommandProvider> localCommandProviders =
                OMTMetaTreeUtil.collectMetaParents(getHost(), YAMLPsiElement.class, OMTLocalCommandProvider.class);

        localCommandProviders.values().stream()
                .map(OMTLocalCommandProvider::getLocalCommandsMap)
                .forEach(map -> map.forEach((key, value) -> nonPsiCallables.put(key, Collections.singleton(value))));

        return nonPsiCallables;
    }

    private HashMap<String, Collection<PsiPrefix>> getPrefixes() {
        HashMap<String, Collection<PsiPrefix>> prefixes = new HashMap<>();

        Map<YAMLMapping, OMTPrefixProvider> prefixProviders =
                OMTMetaTreeUtil.collectMetaParents(getHost(), YAMLMapping.class, OMTPrefixProvider.class);
        prefixProviders.entrySet().stream()
                .map(entry -> entry.getValue().getPrefixMap(entry.getKey()))
                .forEach(prefixSet -> addToCollectionMap(prefixes, prefixSet));
        return prefixes;
    }

    @Override
    public int getLineOffsetInParent() {
        int textOffset = getInjectionStart();
        Document hostDocument = PsiDocumentManager.getInstance(getProject()).getDocument(getHostFile());
        if (hostDocument == null) {
            return 0;
        }

        int lineNumber = hostDocument.getLineNumber(textOffset);
        return textOffset - hostDocument.getLineStartOffset(lineNumber);
    }

    private int getInjectionStart() {
        if (getHost() instanceof YAMLScalarListImpl) {
            List<TextRange> contentRanges = ((YAMLScalarListImpl) getHost()).getTextEvaluator().getContentRanges();
            if (!contentRanges.isEmpty()) {
                return getHost().getTextOffset() + contentRanges.get(0).getStartOffset();
            }
        }
        return getHost().getTextOffset();
    }
}
