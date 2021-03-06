package com.misset.opp.omt.psi.impl;

import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.OMTProcedureMetaType;
import com.misset.opp.omt.meta.model.modelitems.OMTActivityMetaType;
import com.misset.opp.omt.meta.model.modelitems.OMTModelItemDelegateMetaType;
import com.misset.opp.omt.psi.OMTCallable;
import com.misset.opp.resolvable.Callable;
import com.misset.opp.resolvable.Context;
import com.misset.opp.resolvable.psi.PsiCall;
import com.misset.opp.resolvable.psi.PsiCallableImpl;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Wrapper class for YAMLMapping elements that represent a callable OMT item such as ModelItems.
 * OMTCallableImpl can be used to resolve and obtain information about the callable and, if required,
 * will use a meta-type delegate to get more specific information based on the type of Callable.
 * <p>
 * The OMTModelItemMetaType and OTMCallableImpl differ in the sense that the OMTCallableImpl is a non-specific
 * wrapper for the PsiElement.
 * The OMTModelItemMetaType serves as structure check for YAML
 * which needs to be able to instantiate without being wrapped around any PsiElement. It requires to be provided
 * with a YAMLMapping element on method that requires information specific an instance of the MetaType in the
 * PsiTree. Therefore, the OTMCallableImpl is basically a proxy between the PsiCallable and OMTMetaTypes
 */
public class OMTCallableImpl extends PsiCallableImpl implements OMTCallable {

    private final YAMLMapping mapping;
    private final YAMLKeyValue keyValue;

    public OMTCallableImpl(YAMLKeyValue keyValue) {
        super(keyValue.getNode());

        if (!(keyValue.getValue() instanceof YAMLMapping)) {
            throw new RuntimeException("Cannot parse " + keyValue.getValue() + " to map");
        }
        this.keyValue = keyValue;
        this.mapping = (YAMLMapping) keyValue.getValue();
    }

    @Override
    public String getName() {
        return Optional.ofNullable(mapping.getParent())
                .filter(YAMLKeyValue.class::isInstance)
                .map(YAMLKeyValue.class::cast)
                .map(YAMLKeyValue::getKeyText)
                .orElse(null);
    }

    @Override
    public PsiElement getOriginalElement() {
        return keyValue;
    }

    @Override
    public String getType() {
        return computeFromMeta(OMTModelItemDelegateMetaType.class, OMTModelItemDelegateMetaType::getType, "OMT Callable");
    }

    @Override
    public boolean canBeAppliedTo(Set<OntResource> resources) {
        return computeFromMetaCallable(
                omtMetaCallable -> omtMetaCallable.canBeAppliedTo(mapping, resources),
                false);
    }

    @Override
    public int minNumberOfArguments() {
        return computeFromMetaCallable(callable -> callable.minNumberOfArguments(mapping), 0);
    }

    @Override
    public int maxNumberOfArguments() {
        // input parameters are never optional, min and max number are identical
        return computeFromMetaCallable(callable -> callable.maxNumberOfArguments(mapping), 0);
    }

    @Override
    public Map<Integer, String> getParameterNames() {
        return computeFromMetaCallable(callable -> callable.getParameterNames(mapping), new HashMap<>());
    }

    @Override
    public Set<OntResource> getParamType(int index) {
        return getParameterTypes().getOrDefault(index, Collections.emptySet());
    }

    @Override
    public HashMap<Integer, Set<OntResource>> getParameterTypes() {
        return computeFromMetaCallable(callable -> callable.getParameterTypes(mapping), new HashMap<>());
    }

    @Override
    public boolean isVoid() {
        return computeFromMeta(Callable.class, Callable::isVoid, false);
    }

    private OMTMetaType getMetaType() {
        return (OMTMetaType) Optional.ofNullable(OMTMetaTypeProvider.getInstance(mapping.getProject())
                        .getMetaTypeProxy(mapping))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .orElse(null);
    }

    @Override
    public boolean isCommand() {
        return computeFromMeta(OMTMetaType.class,
                metaType -> metaType instanceof OMTActivityMetaType ||
                        metaType instanceof OMTProcedureMetaType,
                false);
    }

    @Override
    public String getCallId() {
        return (isCommand() ? "@" : "") + getName();
    }

    @Override
    public @NotNull Set<OntResource> resolve(Context context) {
        return computeFromMeta(OMTMetaCallable.class,
                omtMetaCallable -> omtMetaCallable.resolve(mapping, context),
                Collections.emptySet());
    }

    private <T, U> U computeFromMeta(Class<T> metaType,
                                     Function<T, U> ifMetaIsPresent,
                                     U orElse) {
        return Optional.ofNullable(getMetaType())
                .filter(omtMetaType -> metaType.isAssignableFrom(omtMetaType.getClass()))
                .map(metaType::cast)
                .map(ifMetaIsPresent)
                .orElse(orElse);
    }

    private <T> void runOnMeta(Class<T> metaType,
                               Consumer<T> ifMetaIsPresent) {
        Optional.ofNullable(getMetaType())
                .filter(omtMetaType -> metaType.isAssignableFrom(omtMetaType.getClass()))
                .map(metaType::cast)
                .ifPresent(ifMetaIsPresent);
    }

    private <U> U computeFromMetaCallable(Function<OMTMetaCallable, U> ifMetaCallableIsPresent, U orElse) {
        return computeFromMeta(OMTMetaCallable.class, ifMetaCallableIsPresent, orElse);
    }

    @Override
    public Set<OntResource> getSecondReturnArgument() {
        return computeFromMetaCallable(OMTMetaCallable::getSecondReturnArgument, Collections.emptySet());
    }

    @Override
    public boolean isStatic() {
        return true;
    }

    @Override
    public List<String> getFlags() {
        return computeFromMetaCallable(OMTMetaCallable::getFlags, Collections.emptyList());
    }

    @Override
    public Set<String> getParamValues(int index) {
        return computeFromMetaCallable(omtMetaCallable -> omtMetaCallable.getAcceptableValues(index), Collections.emptySet());
    }

    @Override
    public void validate(PsiCall call, ProblemsHolder holder) {
        // most of the validations are performed in the abstract superclass and PsiCallable interface methods
        // specific meta-based validation should be added here
        super.validate(call, holder);
        runOnMeta(OMTMetaCallable.class, callable -> callable.validate(mapping, call, holder));
    }

    @Override
    public void validateValue(PsiCall call, ProblemsHolder holder, int i) {
        runOnMeta(OMTMetaCallable.class, callable -> callable.validateValue(call, holder, i));
    }
}
