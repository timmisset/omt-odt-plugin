package com.misset.opp.ttl.style;

import com.intellij.lang.ASTNode;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.ttl.psi.TTLPredicate;
import com.misset.opp.ttl.psi.TTLStubBasedObject;
import com.misset.opp.ttl.psi.TTLSubject;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.misset.opp.ttl.psi.TTLTypes.*;

/**
 * Since the distinction between S, P & O is made in the grammer based on their position in the statement
 * we can only highlight / color them via the annotator and not directly as outcome of the highlighting lexer
 */
public class TTLColorAnnotator implements Annotator {

    @Override
    @SuppressWarnings("java:S2637")
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        IElementType elementType = element.getNode().getElementType();
        if (elementType == IRI) {
            PsiElement firstParent = PsiTreeUtil.findFirstParent(element,
                    parent -> parent instanceof TTLSubject || parent instanceof TTLStubBasedObject || parent instanceof TTLPredicate);
            elementType = Optional.ofNullable(firstParent)
                    .map(PsiElement::getNode)
                    .map(ASTNode::getElementType)
                    .orElse(null);
            if (elementType == SUBJECT) {
                highlight(holder, TTLSyntaxHighlighter.SUBJECTS);
            } else if (elementType == PREDICATE) {
                highlight(holder, TTLSyntaxHighlighter.PREDICATES);
            } else if (elementType == OBJECT) {
                highlight(holder, TTLSyntaxHighlighter.OBJECTS);
            }
        }

    }

    protected void highlight(AnnotationHolder holder, TextAttributesKey key) {
        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(key)
                .create();
    }
}
