package com.misset.opp.odt.annotation;

import com.intellij.lang.annotation.AnnotationHolder;

/**
 * Any OMT element that should be annotate should implement this interface to be targeted by the ODTAnnotator
 * @see ODTAnnotator
 */
public interface ODTAnnotatedElement {
    void annotate(AnnotationHolder holder);
}
