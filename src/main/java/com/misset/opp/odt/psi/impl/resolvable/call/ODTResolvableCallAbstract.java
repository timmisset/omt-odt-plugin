package com.misset.opp.odt.psi.impl.resolvable.call;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.Pair;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.*;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.model.OntologyModel;
import com.misset.opp.odt.ODTElementGenerator;
import com.misset.opp.odt.psi.*;
import com.misset.opp.odt.psi.impl.resolvable.ODTResolvableAbstract;
import com.misset.opp.odt.psi.reference.ODTCallReference;
import com.misset.opp.odt.psi.resolvable.call.ODTCall;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.ContextFactory;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public abstract class ODTResolvableCallAbstract extends ODTResolvableAbstract implements ODTCall {
    private static final Key<CachedValue<Set<OntResource>>> RESOLVED = new Key<>("RESOLVED");
    private final HashMap<String, Set<OntResource>> parameters = new HashMap<>();

    protected ODTResolvableCallAbstract(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public ODTCallReference getReference() {
        return new ODTCallReference(this, getCallName().getTextRangeInParent());
    }

    @Override
    public Callable getCallable() {
        return Optional.ofNullable(getReference())
                .map(ODTCallReference::resolve)
                .filter(Callable.class::isInstance)
                .map(Callable.class::cast)
                .orElseGet(() -> {
                    PsiFile containingFile = getContainingFile();
                    if (!(containingFile instanceof ODTFile)) {
                        return null;
                    }
                    return ((ODTFile) containingFile).getCallables(getCallId()).stream().findFirst().orElse(null);
                });
    }

    @Override
    public String getName() {
        return getCallName().getText();
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        final ODTCallName callName = ODTElementGenerator.getInstance(getProject()).createCallName(name);
        return getCallName().replace(callName);
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return CachedValuesManager.getCachedValue(this, RESOLVED, () -> {
            Context context = ContextFactory.fromCall(this);
            final Set<OntResource> resources = Optional.ofNullable(getCallable())
                    .map(callable -> callable.resolve(context))
                    .orElse(Collections.emptySet());
            PsiFile[] files = context.getFilesInScope().stream().filter(Objects::nonNull).toArray(PsiFile[]::new);
            return new CachedValueProvider.Result<>(resources,
                    // if resolved, only depend on the included files, otherwise stay safe with the entire PsiModificationTracker
                    // this is required to dump the cache after a broken import link is repaired (for example)
                    !resources.isEmpty() ? files : PsiModificationTracker.MODIFICATION_COUNT,
                    OntologyModel.ONTOLOGY_MODEL_MODIFICATION_TRACKER);
        });
    }

    @Override
    public @NotNull List<ODTSignatureArgument> getSignatureArguments() {
        return Optional.ofNullable(getSignature())
                .map(ODTSignature::getSignatureArgumentList)
                .orElse(new ArrayList<>());
    }

    @Override
    public ODTSignatureArgument getSignatureArgument(int index) {
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

        if (query instanceof ODTQueryPath) {
            return ((ODTQueryPath) query).resolveToSubjectPredicate();
        }
        return null;
    }

    @Override
    public int getArgumentIndexOf(PsiElement element) {
        return getSignatureArguments().stream()
                .filter(argument -> PsiTreeUtil.isAncestor(argument, element, false))
                .map(getSignatureArguments()::indexOf)
                .findFirst()
                .orElse(-1);
    }

    @Override
    public void removeArgument(int index) {
        ODTSignature signature = getSignature();
        if (signature == null) {
            return;
        }
        List<ODTSignatureArgument> signatureArgumentList = signature.getSignatureArgumentList();
        String[] filteredArguments = signatureArgumentList.stream().filter(
                        signatureArgument -> signatureArgumentList.indexOf(signatureArgument) != index
                ).map(PsiElement::getText)
                .toArray(String[]::new);
        if (filteredArguments.length == 0) {
            signature.delete();
            return;
        }

        ODTCall call = ODTElementGenerator.getInstance(getProject()).createCall("Call", null, filteredArguments);
        if (call != null && call.getSignature() != null) {
            signature.replace(call.getSignature());
        }
    }

    @Override
    public Set<OntResource> resolvePreviousStep() {
        return Optional.of(getParent())
                .filter(ODTQueryOperationStep.class::isInstance)
                .map(ODTQueryOperationStep.class::cast)
                .map(ODTQueryOperationStep::resolvePreviousStep)
                .orElse(Collections.emptySet());
    }

    @Override
    public boolean isPrimitiveArgument(int index) {
        return Optional.ofNullable(getSignatureArgument(index))
                .map(ODTSignatureArgument::isPrimitiveArgument)
                .orElse(false);
    }
}
