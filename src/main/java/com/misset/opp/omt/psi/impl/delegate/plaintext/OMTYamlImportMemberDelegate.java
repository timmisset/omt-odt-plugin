package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.psi.references.OMTImportMemberReference;
import com.misset.opp.omt.util.OMTRefactoringUtil;
import com.misset.opp.refactoring.SupportsSafeDelete;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.CallableType;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.Resolvable;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallable;
import com.misset.opp.resolvable.psi.PsiReferencedElement;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Literal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The OMTYamlImportMemberDelegate serves as a proxy to the actual Callable that it imports
 * All PsiCallable interface methods are deferred
 */
public class OMTYamlImportMemberDelegate extends OMTYamlPlainTextDelegateAbstract implements
        PsiNamedElement,
        SupportsSafeDelete,
        PsiReferencedElement,
        PsiCallable {
    YAMLPlainTextImpl value;

    public OMTYamlImportMemberDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue.getNode());
        this.value = yamlValue;
    }

    @Override
    public PsiElement setName(@NotNull String newName) {
        final YAMLKeyValue newValue = YAMLElementGenerator.getInstance(this.value.getProject())
                .createYamlKeyValue("foo", newName);
        return value.replace(newValue);
    }

    @Override
    @NotNull
    public OMTImportMemberReference getReference() {
        return new OMTImportMemberReference(value);
    }

    /**
     * Return the PsiCallable element that this import member refers to
     */
    public PsiCallable getFinalElement() {
        PsiElement resolve = getReference().resolve(false);
        if (resolve instanceof OMTYamlImportMemberDelegate) {
            return ((OMTYamlImportMemberDelegate) resolve).getFinalElement();
        } else if (resolve instanceof PsiCallable) {
            return (PsiCallable) resolve;
        }
        return null;
    }

    @Override
    public void delete() throws IncorrectOperationException {
        OMTRefactoringUtil.removeFromSequence(this);
    }

    @Override
    public boolean isUnused() {
        return ReferencesSearch.search(this, getContainingFile().getUseScope()).findFirst() == null;
    }

    @Override
    public PsiElement getOriginalElement() {
        return value;
    }

    // All method calls made to an ImportMember Callable should be deferred to the actual element
    // This is done by resolving this Callable to the member received from the imported file
    // which will return either an actual Callable member or another imported file (i.e. index.omt files)
    private PsiCallable resolveToCallable() {
        return Optional.of(getReference())
                .map(reference -> reference.resolve(false))
                .filter(PsiCallable.class::isInstance)
                .map(PsiCallable.class::cast)
                .orElse(null);
    }

    private <T> T computeFromCallable(Function<PsiCallable, T> ifPresent, T orElse) {
        return Optional.ofNullable(resolveToCallable())
                .map(ifPresent)
                .orElse(orElse);
    }

    private void runOnCallable(Consumer<PsiCallable> ifPresent) {
        Optional.ofNullable(resolveToCallable())
                .ifPresent(ifPresent);
    }

    @Override
    public List<String> getFlags() {
        return computeFromCallable(Callable::getFlags, Collections.emptyList());
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        return computeFromCallable(Callable::getSecondReturnArgument, Collections.emptySet());
    }

    @Override
    public @NotNull Set<OntResource> resolve() {
        return computeFromCallable(Callable::resolve, Collections.emptySet());
    }

    @Override
    public void validate(PsiCall call, ProblemsHolder holder) {
        runOnCallable(callable -> callable.validate(call, holder));
    }

    @Override
    public Set<OntResource> getAcceptableArgumentType(int index, PsiCall call) {
        return computeFromCallable(callable -> callable.getAcceptableArgumentType(index, call), Collections.emptySet());
    }

    @Override
    public Set<OntResource> getAcceptableInputType() {
        return computeFromCallable(Callable::getAcceptableInputType, Collections.emptySet());
    }

    @Override
    public HashMap<Integer, Set<OntResource>> mapCallableParameters(List<Set<OntResource>> resources) {
        return computeFromCallable(callable -> callable.mapCallableParameters(resources), new HashMap<>());
    }

    @Override
    public @NotNull Set<OntResource> resolve(@Nullable Context context) {
        return computeFromCallable(callable -> callable.resolve(context), Collections.emptySet());
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public String getDescription(Project project) {
        return computeFromCallable(callable -> callable.getDescription(project), null);
    }

    @Override
    public String getCallId() {
        return computeFromCallable(Callable::getCallId, getName());
    }

    @Override
    public int minNumberOfArguments() {
        return computeFromCallable(Callable::minNumberOfArguments, -1);
    }

    @Override
    public int maxNumberOfArguments() {
        return computeFromCallable(Callable::maxNumberOfArguments, -1);
    }

    @Override
    public boolean isVoid() {
        return computeFromCallable(Callable::isVoid, false);
    }

    @Override
    public boolean isCommand() {
        return computeFromCallable(Callable::isCommand, false);
    }

    @Override
    public boolean requiresInput() {
        return computeFromCallable(Callable::requiresInput, false);
    }

    @Override
    public HashMap<Integer, Set<OntResource>> getParameterTypes() {
        return computeFromCallable(Callable::getParameterTypes, new HashMap<>());
    }

    @Override
    public Map<Integer, String> getParameterNames() {
        return computeFromCallable(Callable::getParameterNames, new HashMap<>());
    }

    @Override
    public CallableType getType() {
        return computeFromCallable(Callable::getType, CallableType.UNKNOWN);
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return computeFromCallable(callable -> callable.canBeAppliedTo(resources), false);
    }

    @Override
    public boolean isBuiltin() {
        return false;
    }

    @Override
    public Set<OntResource> getParamType(int index) {
        return computeFromCallable(callable -> callable.getParamType(index), Collections.emptySet());
    }

    @Override
    public Set<String> getParamValues(int index) {
        return computeFromCallable(callable -> callable.getParamValues(index), Collections.emptySet());
    }

    @Override
    public void validateValue(PsiCall call, ProblemsHolder holder, int i) {
        runOnCallable(callable -> callable.validateValue(call, holder, i));
    }

    @Override
    public @NotNull List<Literal> resolveLiteral() {
        return computeFromCallable(Resolvable::resolveLiteral, Collections.emptyList());
    }
}
