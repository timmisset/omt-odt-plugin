package com.misset.opp.omt.util;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.FilenameIndex;
import com.misset.opp.callable.psi.PsiCallable;
import com.misset.opp.omt.psi.OMTFile;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;
import org.jetbrains.yaml.psi.YAMLValue;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class OMTModuleUtil {

    public static OMTFile getModule(Project project, String name) {
        PsiManager manager = PsiManager.getInstance(project);
        return FilenameIndex.getAllFilesByExt(project, "module.omt")
                .stream()
                .map(manager::findFile)
                .filter(OMTFile.class::isInstance)
                .map(OMTFile.class::cast)
                .filter(file -> name.equals(file.getModuleName()))
                .findFirst()
                .orElse(null);
    }

    public static List<String> getExportedMemberNames(OMTFile file) {
        return getExportedMembers(file)
                .stream()
                .map(PsiElement::getText)
                .collect(Collectors.toList());
    }

    public static PsiCallable getExportedCallable(OMTFile file, String member) {
        return getExportedMembers(file)
                .stream()
                .filter(value -> member.equals(value.getText()))
                .map(PsiElement::getReference)
                .filter(Objects::nonNull)
                .map(PsiReference::resolve)
                .filter(PsiCallable.class::isInstance)
                .map(PsiCallable.class::cast)
                .findFirst()
                .orElse(null);
    }

    public static YAMLValue getExportedMember(OMTFile file, String member) {
        return OMTModuleUtil.getExportedMembers(file)
                .stream()
                .filter(value -> member.equals(value.getText()))
                .findFirst()
                .orElse(null);
    }

    public static List<YAMLValue> getExportedMembers(OMTFile file) {
        return Optional.ofNullable(file.getRootMapping())
                .map(mapping -> mapping.getKeyValueByKey("export"))
                .map(YAMLKeyValue::getValue)
                .filter(YAMLSequence.class::isInstance)
                .map(YAMLSequence.class::cast)
                .map(YAMLSequence::getItems)
                .stream()
                .flatMap(Collection::stream)
                .map(YAMLSequenceItem::getValue)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
