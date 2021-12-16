package com.misset.opp.omt.meta;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.extapi.psi.ASTDelegatePsiElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.yaml.meta.model.CompletionContext;
import org.jetbrains.yaml.meta.model.YamlStringType;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLMapping;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.impl.YAMLPlainTextImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTExportMemberMetaType extends YamlStringType {

    @Override
    public @NotNull List<? extends LookupElement> getValueLookups(@NotNull YAMLScalar insertedScalar, @Nullable CompletionContext completionContext) {
        YAMLMapping root = PsiTreeUtil.getTopmostParentOfType(insertedScalar, YAMLMapping.class);
        YAMLSequence exportingArray = PsiTreeUtil.getParentOfType(insertedScalar, YAMLSequence.class);
        if (root != null) {
            YAMLKeyValue imports = root.getKeyValueByKey("import");
            if (imports == null) {
                return Collections.emptyList();
            }
            List<String> importMembers = PsiTreeUtil.findChildrenOfType(imports, YAMLPlainTextImpl.class)
                    .stream()
                    .map(ASTDelegatePsiElement::getText)
                    .collect(Collectors.toList());
            List<String> exportedMembers = Optional.ofNullable(exportingArray)
                    .map(YAMLSequence::getItems)
                    .stream()
                    .flatMap(Collection::stream)
                    .map(PsiElement::getText)
                    .collect(Collectors.toList());
            return importMembers.stream()
                    .filter(s -> !exportedMembers.contains(s))
                    .map(LookupElementBuilder::create)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
