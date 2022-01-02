package com.misset.opp.odt.formatter;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiLanguageInjectionHost;
import com.misset.opp.odt.psi.impl.ODTFileImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.yaml.psi.impl.YAMLScalarListImpl;

import java.util.List;

public class ODTHostFormattingUtil {
    public static int getMinimalLineOffset(@NotNull PsiFile file) {
        PsiLanguageInjectionHost host = ((ODTFileImpl) file).getHost();
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
            if (contentRanges.size() > 0) {
                return host.getTextOffset() + contentRanges.get(0).getStartOffset();
            }
        }
        return host.getTextOffset();
    }

    static Document getHostDocument(@NotNull PsiFile file) {
        PsiLanguageInjectionHost host = ((ODTFileImpl) file).getHost();
        return PsiDocumentManager.getInstance(file.getProject()).getDocument(host.getContainingFile());
    }
}
