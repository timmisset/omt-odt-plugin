package com.misset.opp.omt.injection;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.omt.meta.OMTMetaTypeProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.meta.impl.YamlMetaTypeProvider;
import org.jetbrains.yaml.meta.model.YamlMetaType;
import org.jetbrains.yaml.psi.YAMLValue;
import org.jetbrains.yaml.psi.impl.YAMLScalarListImpl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OMTODTInjectionUtil {

    public static PsiLanguageInjectionHost getInjectionHost(PsiElement element) {
        return InjectedLanguageManager.getInstance(element.getProject())
                .getInjectionHost(element);
    }

    public static YamlMetaType getInjectionMetaType(PsiElement element) {
        return Optional.ofNullable(getInjectionHost(element))
                .filter(YAMLValue.class::isInstance)
                .map(YAMLValue.class::cast)
                .map(yamlPsiElement -> OMTMetaTypeProvider.getInstance(element.getProject())
                        .getValueMetaType(yamlPsiElement))
                .map(YamlMetaTypeProvider.MetaTypeProxy::getMetaType)
                .orElse(null);
    }

    public static int getMinimalLineOffset(@NotNull PsiFile file) {
        PsiLanguageInjectionHost host = getInjectionHost(file);
        if (host == null) {
            return 0;
        }
        int textOffset = getInjectionStart(host);
        Document hostDocument = getHostDocument(file);
        if (hostDocument == null) {
            return 0;
        }
        int lineNumber = hostDocument.getLineNumber(textOffset);
        return textOffset - hostDocument.getLineStartOffset(lineNumber);
    }

    private static int getInjectionStart(PsiLanguageInjectionHost host) {
        if (host instanceof YAMLScalarListImpl) {
            List<TextRange> contentRanges = ((YAMLScalarListImpl) host).getTextEvaluator().getContentRanges();
            if (!contentRanges.isEmpty()) {
                return host.getTextOffset() + contentRanges.get(0).getStartOffset();
            }
        }
        return host.getTextOffset();
    }

    public static Document getHostDocument(@NotNull PsiFile file) {
        PsiLanguageInjectionHost host = getInjectionHost(file);
        if (host == null) {
            return null;
        }
        return PsiDocumentManager.getInstance(file.getProject()).getDocument(host.getContainingFile());
    }

    /**
     * Returns the injected elements from the YAMLValue.
     * It's assumed the YAMLValue is an implementation of PsiLanguageInjectionHost and the corresponding MetaType provider
     * is in implementation of OMTInjectable.
     * The root element (Script) of the injected File is searched using the PsiTreeUtil.findChildrenOfType
     */
    public static <T extends PsiElement> Collection<T> getInjectedContent(YAMLValue value,
                                                                          Class<T> contentClass) {
        if (value == null || !value.isValid()) {
            return Collections.emptyList();
        }

        final InjectedLanguageManager instance = InjectedLanguageManager.getInstance(value.getProject());
        final List<Pair<PsiElement, TextRange>> injectedPsiFiles = instance.getInjectedPsiFiles(value);
        return Optional.ofNullable(injectedPsiFiles)
                .map(pairs -> pairs.get(0))
                .map(pair -> pair.getFirst())
                .map(element ->
                        contentClass.isAssignableFrom(element.getClass()) ? Collections.singletonList(contentClass.cast(
                                element)) :
                                PsiTreeUtil.findChildrenOfType(element, contentClass))
                .orElse(Collections.emptyList());
    }
//
//    public static <T extends YAMLPsiElement, U> LinkedHashMap<T, U> getProviders(PsiElement element,
//                                                                                 Class<T> yamlClass,
//                                                                                 Class<U> metaTypeOrInterface) {
//        return Optional.ofNullable(getInjectionHost(element))
//                .map(host -> OMTMetaTreeUtil.collectMetaParents(
//                        host, yamlClass, metaTypeOrInterface, true, Objects::isNull
//                ))
//                .orElse(new LinkedHashMap<>());
//    }
}
