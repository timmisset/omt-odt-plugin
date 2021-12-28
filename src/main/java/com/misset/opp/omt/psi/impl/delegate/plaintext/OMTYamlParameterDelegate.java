package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.psi.references.OMTParamTypePrefixReference;
import com.misset.opp.omt.psi.references.OMTTTLSubjectReference;
import com.misset.opp.omt.util.PatternUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Optional;
import java.util.regex.Matcher;

/**
 * Serves as a proxy between the OMTNamedVariableMetaType types and the PsiElement.
 * It acts as a wrapper element for the YamlPlainTextImpl (Scalar) to provide certain features
 * required by the Variable interface.
 * <p>
 * The MetaTypes are Psi-less classes which require to be called with the actual PsiElement they
 * represent in order to provide the information
 */
public class OMTYamlParameterDelegate extends OMTYamlVariableDelegate {
    YAMLPlainTextImpl value;

    public OMTYamlParameterDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue);
        this.value = yamlValue;
    }

    @Override
    public boolean isParameter() {
        return true;
    }

    private @Nullable TextRange getTypePrefixRange(YAMLPlainTextImpl value) {
        final Matcher matcher = OMTParamMetaType.SHORTHAND_PREFIX_TYPED.matcher(value.getText());
        final boolean b = matcher.find();
        if (b && matcher.groupCount() == 4 && matcher.start(3) > -1) {
            return new TextRange(matcher.start(3), matcher.end(3));
        }
        return null;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        String text = value.getText();
        Optional<TextRange> prefixRange = PatternUtil.getTextRange(text, OMTParamMetaType.SHORTHAND_PREFIX_TYPED, 3);
        Optional<TextRange> localNameRange = PatternUtil.getTextRange(text, OMTParamMetaType.SHORTHAND_PREFIX_TYPED, 4);
        if (prefixRange.isPresent() && localNameRange.isPresent()) {
            return new PsiReference[]{
                    new OMTParamTypePrefixReference(value, prefixRange.get()),
                    new OMTTTLSubjectReference(value, localNameRange.get())
            };
        }

        Optional<TextRange> textRange = PatternUtil.getTextRange(text, OMTParamMetaType.SHORTHAND_URI_TYPED, 1);
        return textRange.map(range -> new PsiReference[]{
                new OMTTTLSubjectReference(value, range)
        }).orElse(PsiReference.EMPTY_ARRAY);
    }
}
