package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.misset.opp.callable.Variable;
import com.misset.opp.callable.global.GlobalVariable;
import com.misset.opp.odt.ODTInjectionUtil;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.reference.ODTVariableReference;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.OMTTypeResolver;
import com.misset.opp.omt.meta.providers.OMTLocalVariableProvider;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class ODTUsedVariableDelegate extends ODTBaseVariableDelegate  {

    public ODTUsedVariableDelegate(ODTVariable element) {
        super(element);
    }

    @Override
    public boolean isDeclaredVariable() {
        return !isAssignmentPart() && isOMTVariableProvider();
    }

    @Override
    public PsiReference getReference() {
        if (isDeclaredVariable()) {
            return null;
        }
        return new ODTVariableReference(element);
    }

    /**
     * A variable can be typed by many different parts of the OMT and ODT languages
     * - via a PsiElement reference result
     * - local variable (either from OMT entry or ODT procedure/command) with context depending type
     * - global variable with static type
     */
    @Override
    public Set<OntResource> getType() {
        return getTypeFromReference()
                .or(this::getTypeFromOMTLocalVariable)
                .or(this::getTypeFromGlobalVariable)
                .or(this::getTypeFromCallContext)
                .orElse(Collections.emptySet());
    }

    private Optional<Set<OntResource>> getTypeFromReference() {
        return Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .filter(ODTVariable.class::isInstance)
                .map(ODTVariable.class::cast)
                .map(this::getType);
    }

    private Optional<Set<OntResource>> getTypeFromOMTLocalVariable() {
        return element.getContainingFile()
                .getProviders(YAMLValue.class, OMTLocalVariableProvider.class)
                .entrySet()
                .stream()
                // map the Provider and YamlPsiElement (mapping) to resolve to set of local variables that are present
                .map(entry -> entry.getValue().getLocalVariableMap(entry.getKey()).get(element.getName()))
                .filter(Objects::nonNull)
                // resolve the type from the local variable
                // the type is set by OMTLocalVariableTypeProvider corresponding with the OMTLocalVariableProvider
                .map(Variable::getType)
                .findFirst();
    }

    private Optional<Set<OntResource>> getTypeFromGlobalVariable() {
        return Optional.ofNullable(GlobalVariable.getVariable(element.getName()))
                .map(Variable::getType);
    }

    private Optional<Set<OntResource>> getTypeFromCallContext() {
        return Optional.empty();
    }

    private Set<OntResource> getType(ODTVariable declared) {
        if (declared.isOMTVariableProvider()) {
            // part of the OMT structure
            return getType((YAMLValue) ODTInjectionUtil.getInjectionHost(declared));
        } else {
            return declared.getType();
        }
    }

    private Set<OntResource> getType(YAMLValue yamlValue) {
        return Optional.ofNullable(OMTMetaTypeProvider.getInstance(yamlValue.getProject())
                .getValueMetaType(yamlValue))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .filter(OMTTypeResolver.class::isInstance)
                .map(OMTTypeResolver.class::cast)
                .map(omtTypeResolver -> omtTypeResolver.getType(yamlValue))
                .or(() -> getTypeFromDestructed(yamlValue))
                .orElse(Collections.emptySet());
    }

    private Optional<Set<OntResource>> getTypeFromDestructed(YAMLValue yamlPsiElement) {
        if(!(yamlPsiElement.getParent().getParent() instanceof YAMLMapping)) { return Optional.empty(); }
        final YAMLMapping mapping = (YAMLMapping) yamlPsiElement.getParent().getParent();
        return Optional.ofNullable(OMTMetaTypeProvider.getInstance(mapping.getProject())
                        .getMetaTypeProxy(mapping))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .filter(OMTTypeResolver.class::isInstance)
                .map(OMTTypeResolver.class::cast)
                .map(omtTypeResolver -> omtTypeResolver.getTypeFromDestructed(mapping));
    }
}
