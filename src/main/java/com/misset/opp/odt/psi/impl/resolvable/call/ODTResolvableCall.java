package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.*;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.builtin.commands.BuiltinCommands;
import com.misset.opp.odt.builtin.operators.BuiltinOperators;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.ODTASTWrapperPsiElement;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvable;
import com.misset.opp.odt.psi.impl.resolvable.query.ODTResolvableQueryPath;
import com.misset.opp.odt.psi.reference.ODTCallReference;
import com.misset.opp.omt.meta.providers.OMTLocalCommandProvider;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.local.LocalCommand;
import com.misset.opp.ttl.OppModel;
import com.misset.opp.util.LoggerUtil;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;
import java.util.stream.Collectors;

import static com.misset.opp.omt.meta.OMTMetaTreeUtil.collectLocalCommandProviders;

public abstract class ODTResolvableCall extends ODTASTWrapperPsiElement implements ODTCall, ODTResolvable {
    public ODTResolvableCall(@NotNull ASTNode node) {
        super(node);
    }

    private static final Key<CachedValue<Callable>> CALLABLE = new Key<>("CALLABLE");
    private static final Key<CachedValue<Set<OntResource>>> RESOLVED = new Key<>("RESOLVED");
    private final HashMap<String, Set<OntResource>> parameters = new HashMap<>();
    private String localCommandProvider = null;

    Logger LOGGER = Logger.getInstance(ODTResolvableCall.class);

    @Override
    public ODTCallReference getReference() {
        return new ODTCallReference(this, getCallName().getTextRangeInParent());
    }

    @NotNull
    public abstract ODTCallName getCallName();

    /**
     * Distinct name for the call, when calling a command it will include the AT(@) symbol
     */
    public abstract String getCallId();

    public Callable getCallable() {
        return CachedValuesManager.getCachedValue(this, CALLABLE, () -> new CachedValueProvider.Result<>(calculateCallable(),
                getContainingFile(),
                PsiModificationTracker.MODIFICATION_COUNT));
    }

    private Callable calculateCallable() {
        return LoggerUtil.computeWithLogger(LOGGER, "Getting callable for call " + getCallId(),
                () -> getBuiltin()
                        .or(this::getLocalCommand)
                        .or(this::resolveFromReference)
                        .orElse(null));
    }

    private Optional<Callable> resolveFromReference() {
        return Optional.ofNullable(getReference())
                .map(odtCallReference -> odtCallReference.resolve(false))
                .filter(Callable.class::isInstance)
                .map(Callable.class::cast);
    }

    @Override
    public String getName() {
        return getCallName().getText();
    }

    private Optional<Callable> getLocalCommand() {
        final PsiLanguageInjectionHost injectionHost = ODTInjectionUtil.getInjectionHost(this);
        if (injectionHost == null) {
            return Optional.empty();
        }

        final LinkedHashMap<YAMLMapping, OMTLocalCommandProvider> linkedHashMap = collectLocalCommandProviders(
                injectionHost);
        for (YAMLMapping mapping : linkedHashMap.keySet()) {
            OMTLocalCommandProvider callableProvider = linkedHashMap.get(mapping);
            final HashMap<String, LocalCommand> callableMap = callableProvider.getLocalCommandsMap();
            if (callableMap.containsKey(getCallId())) {
                localCommandProvider = callableProvider.getType();
                return Optional.of(callableMap.get(getCallId()));
            }
        }
        return Optional.empty();
    }

    @Override
    public String getLocalCommandProvider() {
        return localCommandProvider;
    }

    private Optional<Callable> getBuiltin() {
        return Optional.ofNullable(BuiltinCommands.builtinCommands.get(getCallId()))
                .or(() -> Optional.ofNullable(BuiltinOperators.get(getCallId())));
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        final ODTCallName callName = ODTElementGenerator.getInstance(getProject()).createCallName(name);
        return getCallName().replace(callName);
    }


    @Override
    public @NotNull Set<OntResource> resolve() {
        return CachedValuesManager.getCachedValue(this, RESOLVED, () -> {
            final Set<OntResource> resources = Optional.ofNullable(getCallable())
                    .map(callable -> callable.resolve(resolvePreviousStep(), this))
                    .orElse(Collections.emptySet());
            return new CachedValueProvider.Result<>(resources,
                    getContainingFile(),
                    PsiModificationTracker.MODIFICATION_COUNT,
                    OppModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER);
        });
    }

    @Override
    public @NotNull List<ODTResolvableSignatureArgument> getSignatureArguments() {
        return Optional.ofNullable(getSignature())
                .map(ODTSignature::getSignatureArgumentList)
                .stream()
                .flatMap(Collection::stream)
                .map(ODTResolvableSignatureArgument.class::cast)
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable ODTResolvableSignatureArgument getSignatureArgument(int index) {
        return Optional.of(getSignatureArguments())
                .filter(list -> list.size() > index)
                .map(list -> list.get(index))
                .orElse(null);
    }

    @Override
    public @NotNull Set<OntResource> resolveSignatureArgument(int index) {
        return Optional.ofNullable(getSignatureArgument(index))
                .map(ODTSignatureArgument::resolve)
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
    public PsiElement getFlagElement() {
        return getFlagSignature();
    }

    @Override
    public int getNumberOfArguments() {
        return getSignatureArguments().size();
    }

    @Override
    public @NotNull List<Set<OntResource>> resolveSignatureArguments() {
        return Collections.emptyList();
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

    @Override
    public PsiElement getCallSignatureElement() {
        return getSignature();
    }

    @Override
    public PsiElement getCallSignatureArgumentElement(int index) {
        return getSignatureArgument(index);
    }

    @Override
    public @Nullable Pair<Set<OntResource>, Property> getSignatureLeadingInformation(int signatureArgument) {
        final ODTQuery query = Optional.ofNullable(getSignatureArgument(signatureArgument))
                .map(ODTSignatureArgument::getResolvableValue)
                .map(ODTResolvableValue::getQuery)
                .stream().findFirst().orElse(null);
        if (query == null) {
            return null;
        }

        if (query instanceof ODTResolvableQueryPath) {
            return ((ODTResolvableQueryPath) query).resolveToSubjectPredicate();
        }
        return null;
    }

    @Override
    public Set<OntResource> resolveCallInput() {
        return resolvePreviousStep();
    }

    @Override
    public int getArgumentIndexOf(PsiElement element) {
        return getSignatureArguments().stream()
                .filter(argument -> PsiTreeUtil.isAncestor(argument, element, true))
                .map(getSignatureArguments()::indexOf)
                .findFirst()
                .orElse(-1);
    }
}
