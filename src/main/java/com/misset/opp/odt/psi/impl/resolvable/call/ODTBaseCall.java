package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.CachedValue;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.callable.Callable;
import com.misset.opp.callable.Resolvable;
import com.misset.opp.callable.builtin.commands.BuiltinCommands;
import com.misset.opp.callable.builtin.operators.BuiltinOperators;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.ODTDefineName;
import com.misset.opp.odt.psi.ODTSignature;
import com.misset.opp.odt.psi.ODTSignatureArgument;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.odt.psi.impl.callable.ODTDefineStatement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.reference.ODTCallReference;
import com.misset.opp.omt.meta.providers.OMTLocalCommandProvider;
import com.misset.opp.omt.psi.impl.OMTCallable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLPsiElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.misset.opp.omt.meta.OMTMetaTreeUtil.collectLocalCommandProviders;

public abstract class ODTBaseCall extends ODTASTWrapperPsiElement implements ODTCall, ODTResolvable {
    public ODTBaseCall(@NotNull ASTNode node) {
        super(node);
    }

    private static final Key<CachedValue<Callable>> CALLABLE = new Key("CALLABLE");
    private final HashMap<String, Set<OntResource>> parameters = new HashMap<>();

    @Override
    public PsiReference getReference() {
        return new ODTCallReference(this, getCallName().getTextRangeInParent());
    }

    @NotNull
    public abstract ODTCallName getCallName();

    /**
     * Distinct name for the call, when calling a command it will include the AT(@) symbol
     */
    public abstract String getCallId();

    public Callable getCallable() {
        return CachedValuesManager.getCachedValue(this, CALLABLE, () -> {
            final Callable callable = Optional.ofNullable(getReference())
                    .map(PsiReference::resolve)
                    .map(this::getCallable)
                    .or(this::getLocalCommand)
                    .or(this::getBuiltin)
                    .orElse(null);
            return new CachedValueProvider.Result<>(callable,
                    callable instanceof PsiElement || callable == null ?
                            PsiModificationTracker.MODIFICATION_COUNT :
                            ModificationTracker.NEVER_CHANGED);
        });
    }

    @Override
    public String getName() {
        return getCallName().getText();
    }

    private Callable getCallable(PsiElement element) {
        if (element instanceof YAMLKeyValue) { // resolves to OMT !Activity, !Procedure etc
            return Optional.of((YAMLKeyValue) element)
                    .map(YAMLKeyValue::getValue)
                    .filter(YAMLMapping.class::isInstance)
                    .map(YAMLMapping.class::cast)
                    .map(OMTCallable::new)
                    .orElse(null);
        } else if (element instanceof ODTDefineName) {
            return (ODTDefineStatement) element.getParent();
        } else {
            return null;
        }
    }

    private Optional<Callable> getLocalCommand() {
        final YAMLPsiElement injectionHost = ODTInjectionUtil.getInjectionHost(this);
        if (injectionHost == null) {
            return Optional.empty();
        }

        final LinkedHashMap<YAMLMapping, OMTLocalCommandProvider> linkedHashMap = collectLocalCommandProviders(
                injectionHost);
        for (YAMLMapping mapping : linkedHashMap.keySet()) {
            OMTLocalCommandProvider callableProvider = linkedHashMap.get(mapping);
            final HashMap<String, Callable> callableMap = callableProvider.getLocalCommandsMap();
            if (callableMap.containsKey(getCallId())) {
                return Optional.of(callableMap.get(getCallId()));
            }
        }
        return Optional.empty();
    }

    private Optional<Callable> getBuiltin() {
        return Optional.ofNullable(BuiltinCommands.builtinCommands.get(getCallId()))
                .or(() -> Optional.ofNullable(BuiltinOperators.builtinOperators.get(getCallId())));
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        final ODTCallName callName = ODTElementGenerator.getInstance(getProject()).createCall(name).getCallName();
        return getCallName().replace(callName);
    }

    @Override
    public String getDocumentation() {
        return Optional.ofNullable(getCallable())
                .map(callable -> callable.getDescription(null))
                .orElse(null);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return Optional.ofNullable(getCallable())
                .map(callable -> callable.resolve(resolvePreviousStep(), this))
                .orElse(Collections.emptySet());
    }

    @Override
    public @NotNull List<ODTSignatureArgument> getSignatureArguments() {
        return Optional.ofNullable(getSignature())
                .map(ODTSignature::getSignatureArgumentList)
                .orElse(Collections.emptyList());
    }

    @Override
    public @Nullable ODTSignatureArgument getSignatureArgument(int index) {
        return Optional.of(getSignatureArguments())
                .filter(list -> list.size() > index)
                .map(list -> list.get(index))
                .orElse(null);
    }

    @Override
    public @NotNull Set<OntResource> resolveSignatureArgument(int index) {
        return Optional.ofNullable(getSignatureArgument(index))
                .map(ODTSignatureArgument::getResolvableValue)
                .map(Resolvable::resolve)
                .orElse(Collections.emptySet());
    }

    @Override
    public void inspect(ProblemsHolder holder) {

    }

    @Override
    public void annotate(AnnotationHolder holder) {

    }

    @Override
    public @Nullable String getFlag() {
        return Optional.ofNullable(getFlagSignature()).map(PsiElement::getText).orElse(null);
    }

    @Override
    public int numberOfArguments() {
        return getSignatureArguments().size();
    }

    @Override
    public List<Set<OntResource>> resolveSignatureArguments() {
        return null;
    }

    @Override
    @Nullable
    public String getSignatureValue(int index) {
        final List<String> signatureValues = getSignatureValues();

        return signatureValues.size() > index ? signatureValues.get(index) : null;
    }

    @Override
    public List<String> getSignatureValues() {
        return getSignatureArguments().stream().map(PsiElement::getText).collect(Collectors.toList());
    }

    @Override
    public Set<OntResource> getParamType(String paramName) {
        return parameters.getOrDefault(paramName, Collections.emptySet());
    }

    @Override
    public void setParamType(String paramName,
                             Set<OntResource> type) {
        parameters.put(paramName, type);
    }
}
