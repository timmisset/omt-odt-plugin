package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.psi.impl.delegate.OMTYamlDelegate;
import com.misset.opp.omt.psi.references.OMTParamTypePrefixReference;
import com.misset.opp.omt.psi.references.OMTTTLSubjectReference;
import com.misset.opp.omt.util.PatternUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.YAMLElementGenerator;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import static com.misset.opp.omt.meta.scalars.OMTParamTypeType.CURIE_PATTERN;
import static com.misset.opp.omt.meta.scalars.OMTParamTypeType.URI_PATTERN;

public class OMTYamlParamTypeDelegate extends YAMLPlainTextImpl implements OMTYamlDelegate {
    YAMLPlainTextImpl value;

    public OMTYamlParamTypeDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue.getNode());
        this.value = yamlValue;
    }

    @Override
    public String getName() {
        return getText();
    }

    @Override
    public PsiElement setName(@NotNull String newName) {
        final YAMLKeyValue newValue = YAMLElementGenerator.getInstance(this.value.getProject())
                .createYamlKeyValue("foo", newName);
        return replace(newValue);
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        String textValue = value.getTextValue();
        if (URI_PATTERN.matcher(textValue).find()) {
            // a URI pattern only has a reference to the Ontology
            return new PsiReference[]{
                    new OMTTTLSubjectReference(value, PatternUtil.getTextRange(textValue, URI_PATTERN, 1)
                            .orElse(TextRange.EMPTY_RANGE))
            };
        } else if (CURIE_PATTERN.matcher(textValue).find()) {
            // a Curie pattern has a reference to the Prefix and the Ontology
            return new PsiReference[]{
                    new OMTParamTypePrefixReference(value, PatternUtil.getTextRange(textValue, CURIE_PATTERN, 1)
                            .orElse(TextRange.EMPTY_RANGE)),
                    new OMTTTLSubjectReference(value, PatternUtil.getTextRange(textValue, CURIE_PATTERN, 2)
                            .orElse(TextRange.EMPTY_RANGE))
            };
        } else {
            return PsiReference.EMPTY_ARRAY;
        }
    }
}
