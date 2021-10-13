package com.misset.opp.omt.psi;

import com.intellij.psi.PsiFile;
import com.misset.opp.omt.psi.model.OMTModelBlock;

public interface OMTFile extends PsiFile {

    OMTModelBlock getModelBlock();
}
