package com.misset.opp.odt.psi.impl.variable.delegate;

import com.intellij.psi.PsiReference;
import com.misset.opp.odt.ODTMultiHostInjector;
import com.misset.opp.odt.psi.ODTVariable;
import com.misset.opp.odt.psi.reference.ODTVariableReference;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.OMTTypeResolver;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
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
        if(isDeclaredVariable()) { return null; }
        return new ODTVariableReference(element);
    }

    @Override
    public Set<OntResource> getType() {
        return Optional.ofNullable(getReference())
                .map(PsiReference::resolve)
                .filter(ODTVariable.class::isInstance)
                .map(ODTVariable.class::cast)
                .map(this::getType)
                .orElse(null);
    }

    private Set<OntResource> getType(ODTVariable declared) {
        if(declared.isOMTVariableProvider()) {
            // part of the OMT structure
            return getType((YAMLValue) ODTMultiHostInjector.getInjectionHost(declared));
        } else {
            return Collections.emptySet();
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
