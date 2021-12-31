package com.misset.opp.omt.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import org.jetbrains.yaml.YAMLElementTypes;
import org.jetbrains.yaml.psi.YAMLKeyValue;
import org.jetbrains.yaml.psi.YAMLSequence;
import org.jetbrains.yaml.psi.YAMLSequenceItem;

public class OMTRefactorUtil {

    public static void removeEOLToken(PsiElement element) {
        PsiElement nextLeaf = PsiTreeUtil.nextLeaf(element);
        if (nextLeaf != null && YAMLElementTypes.EOL_ELEMENTS.contains(PsiUtilCore.getElementType(nextLeaf))) {
            nextLeaf.delete();
        }
    }

    public static void removeFromSequence(PsiElement element) {
        YAMLSequenceItem sequenceItem = PsiTreeUtil.getParentOfType(element, YAMLSequenceItem.class);
        YAMLSequence sequence = PsiTreeUtil.getParentOfType(element, YAMLSequence.class);
        if (sequence == null || sequenceItem == null) {
            return;
        }
        if (sequence.getItems().size() == 1) {
            // only 1 parameter, remove entire block
            YAMLKeyValue sequenceContainer = PsiTreeUtil.getParentOfType(sequence, YAMLKeyValue.class);
            if (sequenceContainer != null) {
                sequenceContainer.delete();
            }
        } else {
            // remove the parameter
            OMTRefactorUtil.removeEOLToken(sequenceItem);
            sequenceItem.delete();
        }
    }
}
