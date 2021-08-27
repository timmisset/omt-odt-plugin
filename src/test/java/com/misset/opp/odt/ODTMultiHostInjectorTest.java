package com.misset.opp.odt;

import com.intellij.lang.injection.InjectedLanguageManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.misset.opp.odt.psi.ODTCallName;
import com.misset.opp.odt.psi.ODTCommandCall;
import com.misset.opp.testCase.OMTTestCase;
import org.jetbrains.yaml.psi.YAMLScalar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ODTMultiHostInjectorTest extends OMTTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void testInjectODTInOnStart() {
        configureByText(insideActivityWithPrefixes(
                "onStart: |\n" +
                        "   @<caret>LOG('test');"
        ));
        ReadAction.run(() -> {
            final ODTCommandCall commandCall = myFixture.findElementByText("@LOG", ODTCommandCall.class);
            Assertions.assertNotNull(commandCall);
        });

    }
}
