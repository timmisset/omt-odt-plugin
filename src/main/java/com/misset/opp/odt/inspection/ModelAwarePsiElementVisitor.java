package com.misset.opp.odt.inspection;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import com.misset.opp.ttl.OppModel;
import org.jetbrains.annotations.NotNull;

/**
 * When updating the model, inspections using the model should not be run
 * todo: check if there is a way to utilize the Dumb/Smart state of the application
 */
public class ModelAwarePsiElementVisitor extends PsiElementVisitor {

    @Override
    public void visitFile(@NotNull PsiFile file) {
        OppModel.INSTANCE.runWithReadLock("Modelaware visitor", () -> super.visitFile(file));
    }
}
