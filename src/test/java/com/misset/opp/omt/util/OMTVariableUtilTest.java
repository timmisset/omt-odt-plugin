package com.misset.opp.omt.util;

import com.intellij.psi.PsiFile;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OMTVariableUtilTest extends OMTTestCase {

    @Test
    void getAccessibleVariablesFromModelItem() {
        String content = insideActivityWithPrefixes(
                "variables:\n" +
                        "- $variableA\n" +
                        "\n" +
                        "onStart:\n"
        );
        final PsiFile psiFile = configureByText(content);

    }
}
