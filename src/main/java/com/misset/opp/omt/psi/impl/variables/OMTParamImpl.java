package com.misset.opp.omt.psi.impl.variables;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTVariable;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLSequenceItemImpl;

import java.util.Collections;
import java.util.List;

/**
 * An OMT (input) parameter can be declared with a type in a shorthand construction
 * -    $variableName (string)
 * or destructed as
 * -    name: $variableName
 *      type: string
 */
public class OMTParamImpl extends YAMLSequenceItemImpl implements OMTVariable {

    public OMTParamImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public List<Resource> getDefaultValue() {
        return Collections.emptyList();
    }
}
