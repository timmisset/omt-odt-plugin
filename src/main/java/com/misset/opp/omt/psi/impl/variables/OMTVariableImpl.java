package com.misset.opp.omt.psi.impl.variables;

import com.intellij.lang.ASTNode;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.omt.psi.impl.DestructableYamlSequenceItem;
import com.misset.opp.ttl.ResourceUtil;
import org.apache.jena.rdf.model.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;
import org.jetbrains.yaml.psi.impl.YAMLSequenceItemImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.misset.opp.ttl.ResourceUtil.fromXsd;

/**
 * An OMT variable can be declared with a default value in a shorthand construction
 * -    $variableName = 'hello world'
 * or destructed as
 * -    name:       $variableName
 *      value:      'hello world'
 *      readOnly:   true || false
 *      onChange:   | someScript containing $newValue and $oldValue as local values
 */
public class OMTVariableImpl extends DestructableYamlSequenceItem implements OMTVariable {
    private static final Pattern NAME_CAPTURE = Pattern.compile("\\$([^\\s]*)");

    public OMTVariableImpl(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    @Nullable
    public String getName() {
        return getName(getValueFor(NAME_KEY));
    }

    @Override
    public boolean isReadOnly() {
        return "true".equals(getDestructedValue(READ_ONLY_KEY));
    }

    @Override
    public List<Resource> getDefaultValue() {
        return Optional.ofNullable(getDestructedValue(DEFAULT_VALUE_KEY))
                // todo: only supports primitives now
                .map(ResourceUtil::fromXsd)
                .orElse(Collections.emptyList());
    }

    private String getName(@Nullable YAMLPlainTextImpl plainText) {
        if (plainText == null) {
            return null;
        }
        Matcher matcher = NAME_CAPTURE.matcher(plainText.getTextValue());
        final boolean b = matcher.find();
        return b ? matcher.group(1) : null;
    }



}
