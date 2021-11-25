package com.misset.opp.omt.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.psi.PsiElement;
import com.misset.opp.callable.Call;
import com.misset.opp.callable.Callable;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.OMTMetaType;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import com.misset.opp.omt.meta.OMTProcedureMetaType;
import com.misset.opp.omt.meta.model.modelitems.OMTActivityMetaType;
import com.misset.opp.omt.psi.OMTCallable;
import org.apache.jena.ontology.OntResource;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Wrapper class for YAMLMapping elements that represent a callable OMT item such as ModelItems.
 * OMTCallableImpl can be used to resolve and obtain information about the callable and, if required,
 * will use a meta-type delegate to get more specific information based on the type of Callable.
 * <p>
 * The OMTModelItemMetaType and OTMCallableImpl differ in the sense that the OMTCallableImpl is a specific
 * wrapper for the PSI tree element while the OMTModelItemMetaType serves as structure check for YAML
 * which needs to be able to instantiate without being wrapped around any PSI Element.
 */
public class OMTCallableImpl extends ASTWrapperPsiElement implements OMTCallable {
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

    private String getType() {
        return Optional.ofNullable(mapping.getParent())
                .filter(YAMLKeyValue.class::isInstance)
                .map(YAMLKeyValue.class::cast)
                .map(YAMLKeyValue::getValue)
                .map(YAMLValue::getTag)
                .map(element -> element.getText().substring(1))
                .orElse(null);
    }

    @Override
    public String getDescription(String context) {
        return getName() + " (" + getType() + ")";
    }

    @Override
    public int minNumberOfArguments() {
        return Optional.ofNullable(mapping.getKeyValueByKey("params"))
                .map(YAMLKeyValue::getValue)
                .filter(YAMLSequence.class::isInstance)
                .map(YAMLSequence.class::cast)
                .map(sequence -> sequence.getItems().size())
                .orElse(0);
    }

    @Override
    public int maxNumberOfArguments() {
        // input parameters are never optional, min and max number are identical
        return minNumberOfArguments();
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
    public Set<OntResource> resolve(Set<OntResource> resources,
                                    Call call) {
        return resolve(mapping, resources, call);
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

    @Override
    public Set<OntResource> resolve(YAMLMapping mapping,
                                    Set<OntResource> resources,
                                    Call call) {
        return computeFromMeta(OMTMetaCallable.class,
                omtMetaCallable -> omtMetaCallable.resolve(mapping, resources, call),
                Collections.emptySet());
    }

    @Override
    public PsiElement getCallTarget() {
        return keyValue.getKey();
    }
}
