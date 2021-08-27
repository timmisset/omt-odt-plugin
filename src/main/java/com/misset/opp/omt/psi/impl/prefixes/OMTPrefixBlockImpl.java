package com.misset.opp.omt.psi.impl.prefixes;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.psi.OMTResettable;
import com.misset.opp.omt.psi.prefixes.OMTPrefix;
import com.misset.opp.omt.psi.prefixes.OMTPrefixBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTPrefixBlockImpl extends YAMLKeyValueImpl implements OMTPrefixBlock, OMTResettable {
    private List<OMTPrefix> prefixes;
    public OMTPrefixBlockImpl(@NotNull ASTNode node) {
        super(node);
    }


    public YAMLMapping getMap() {
        return Optional.ofNullable(getValue())
                .filter(YAMLMapping.class::isInstance)
                .map(YAMLMapping.class::cast)
                .orElse(null);
    }

    @Override
    public List<OMTPrefix> getPrefixes() {
        if(prefixes != null) { return prefixes; }
        prefixes = Optional.ofNullable(getMap())
                .map(YAMLMapping::getKeyValues)
                .orElse(Collections.emptyList())
                .stream()
                .map(PsiElement::getNode)
                .map(OMTPrefixImpl::new)
                .collect(Collectors.toList());
        return prefixes;
    }

    @Override
    public OMTPrefix getPrefix(String prefix) {
        return getPrefixes().stream()
                .filter(omtPrefix -> omtPrefix.isPrefix(prefix))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean hasPrefix(String prefix) {
        return getPrefix(prefix) != null;
    }

    @Override
    public void reset() {
        prefixes = null;
    }
}
