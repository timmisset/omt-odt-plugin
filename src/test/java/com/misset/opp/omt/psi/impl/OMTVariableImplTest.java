package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.application.WriteAction;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OMTVariableImplTest extends OMTTestCase {

    @Test
    void testGetNameShorthand() {
        String content = insideActivityWithPrefixes("variables:\n" +
                "- $variable");
        configureByText(content);

        assertName();
    }

    @Test
    void testGetNameShorthandWithDefaultValue() {
        String content = insideActivityWithPrefixes("variables:\n" +
                "- $variable = true");
        configureByText(content);

        assertName();
    }

    @Test
    void testGetNameDestructed() {
        String content = insideActivityWithPrefixes("variables:\n" +
                "- name: $variable\n" +
                "  default: true\n");
        configureByText(content);

        assertName();
    }


    private void assertName() {
        ReadAction.run(() -> {
            final OMTFile omtFile = getOMTFile();
            final OMTVariable variable = omtFile.getModelBlock()
                    .getModelItems()
                    .get(0)
                    .getDeclaredVariables()
                    .get(0);
            Assertions.assertEquals("variable", variable.getName());
        });
    }

}
