package com.misset.opp.omt.psi.impl.delegate.plaintext;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.misset.opp.omt.meta.OMTMetaCallable;
import com.misset.opp.omt.meta.OMTMetaTreeUtil;
import com.misset.opp.omt.meta.model.variables.OMTParamMetaType;
import com.misset.opp.omt.psi.references.OMTParamTypePrefixReference;
import com.misset.opp.omt.psi.references.OMTTTLSubjectReference;
import com.misset.opp.omt.util.OMTRefactoringUtil;
import com.misset.opp.omt.util.PatternUtil;
import com.misset.opp.refactoring.SupportsSafeDelete;
import com.misset.opp.resolvable.psi.PsiCall;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("java:S110")
public class OMTYamlParameterDelegate extends OMTYamlVariableDelegate implements SupportsSafeDelete {
    YAMLPlainTextImpl yamlPlainText;

    public OMTYamlParameterDelegate(@NotNull YAMLPlainTextImpl yamlValue) {
        super(yamlValue);
        this.yamlPlainText = yamlValue;
    }

    @Override
    public boolean isParameter() {
        return true;
    }

    @Override
    public PsiReference @NotNull [] getReferences() {
        String text = yamlPlainText.getText();
        Optional<TextRange> prefixRange = PatternUtil.getTextRange(text, OMTParamMetaType.SHORTHAND_PREFIX_TYPED, 3);
        Optional<TextRange> localNameRange = PatternUtil.getTextRange(text, OMTParamMetaType.SHORTHAND_PREFIX_TYPED, 4);
        if (prefixRange.isPresent() && localNameRange.isPresent()) {
            return new PsiReference[]{
                    new OMTParamTypePrefixReference(yamlPlainText, prefixRange.get()),
                    new OMTTTLSubjectReference(yamlPlainText, localNameRange.get())
            };
        }

        Optional<TextRange> textRange = PatternUtil.getTextRange(text, OMTParamMetaType.SHORTHAND_URI_TYPED, 1);
        return textRange.map(range -> new PsiReference[]{
                new OMTTTLSubjectReference(yamlPlainText, range)
        }).orElse(PsiReference.EMPTY_ARRAY);
    }

    @Override
    public void delete() throws IncorrectOperationException {
        // remove this parameter and all parameters that are part of calls made to the owner of this parameter:
        Map.Entry<YAMLMapping, OMTMetaCallable> callableEntry =
                OMTMetaTreeUtil.collectMetaParents(this.yamlPlainText, YAMLMapping.class, OMTMetaCallable.class)
                        .entrySet().stream().findFirst().orElse(null);
        int parameterIndex = getParameterIndex();
        if (parameterIndex == -1) {
            return;
        }

        if (callableEntry != null) {
            YAMLKeyValue callable = PsiTreeUtil.getParentOfType(callableEntry.getKey(), YAMLKeyValue.class);
            if (callable == null) {
                return;
            }
            ReferencesSearch.search(callable, callable.getUseScope())
                    .findAll()
                    .stream()
                    .map(PsiReference::getElement)
                    .filter(PsiCall.class::isInstance)
                    .map(PsiCall.class::cast)
                    .forEach(call -> call.removeArgument(parameterIndex));
        }
        OMTRefactoringUtil.removeFromSequence(yamlPlainText);
    }

    private int getParameterIndex() {
        YAMLSequenceItem sequenceItem = PsiTreeUtil.getParentOfType(yamlPlainText, YAMLSequenceItem.class);
        YAMLSequence sequence = PsiTreeUtil.getParentOfType(yamlPlainText, YAMLSequence.class);
        if (sequence == null || sequenceItem == null) {
            return -1;
        }
        return sequence.getItems().indexOf(sequenceItem);
    }

    @Override
    public String getType() {
        return "parameter";
    }

    @Override
    public String getSource() {
        return "OMT parameter";
    }

}
