package com.misset.opp.odt;

import com.intellij.openapi.application.ReadAction;
import com.intellij.psi.PsiElement;
import com.misset.opp.omt.testcase.OMTTestCase;
import com.misset.opp.resolvable.Variable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ODTGlobalVariableTest extends OMTTestCase {

    @ParameterizedTest
    @ValueSource(strings = {
            "$username",
            "$user",
            "$rollen",
            "$medewerkerGraph",
            "$offline",
            "$heeftPreviewRol",
            "$activityConfig",
            "$geoApiKey",
    })
        // six numbers
    void testGlobalVariableIsRecognized(String variableName) {
        String content = insideProcedureRunWithPrefixes("<caret>" + variableName);
        configureByText(content);
        ReadAction.run(() -> {
            PsiElement elementAtCaret = myFixture.getElementAtCaret();
            Assertions.assertTrue(elementAtCaret instanceof Variable);
            Variable variable = (Variable) elementAtCaret;
            Assertions.assertTrue(variable.isGlobal());
            Assertions.assertTrue(variable.isReadonly());
        });
    }

}
