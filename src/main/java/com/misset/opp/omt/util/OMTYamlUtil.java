package com.misset.opp.omt.util;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.YAMLTokenTypes;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLKeyValueImpl;

import java.util.Arrays;
import java.util.Optional;

/**
 * Requirements that are missing / misbehaving in Yaml(Util) are added here
 */
public class OMTYamlUtil {

    private static final TokenSet TAG = TokenSet.create(YAMLTokenTypes.TAG);

    /**
     * Returns the tag identifier of a YAML keyValue, even when the value is empty
     */
    @Nullable
    public static String getTagIdentifier(@Nullable YAMLKeyValue keyValue) {
        if (keyValue == null) {
            return null;
        }
        // get via Yaml
        final String tagIdentifier = Optional.of(keyValue)
                .map(YAMLKeyValue::getValue)
                .map(YAMLValue::getTag)
                .map(PsiElement::getText)
                .orElse(null);
        if(tagIdentifier != null) { return tagIdentifier.substring(1); }

        // get via text
        return Arrays.stream(keyValue.getNode()
                        .getChildren(TAG))
                .map(ASTNode::getText)
                .map(s -> s.substring(1))
                .findFirst()
                .orElse(null);
    }

}
