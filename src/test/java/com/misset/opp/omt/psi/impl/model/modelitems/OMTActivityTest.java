package com.misset.opp.omt.psi.impl.model.modelitems;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.omt.psi.OMTModelItemType;
import com.misset.opp.omt.psi.OMTVariable;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTActivityTest extends OMTTestCase {

    @Test
    void getType() {
        String content = insideActivityWithPrefixes("" +
                "variables:\n" +
                "- $variableA");
        final OMTFile omtFile = configureByText(content);
        ReadAction.run(() -> Assertions.assertEquals(omtFile.getModelBlock().getModelItems().get(0).getType(), OMTModelItemType.Activity));
    }

    @Test
    void getDeclaredVariablesReturnsVariablesBlock() {
        String content = insideActivityWithPrefixes("" +
                "variables:\n" +
                "- $variableA");
        final OMTFile omtFile = configureByText(content);
        ReadAction.run(() -> {
            final List<OMTVariable> variables = omtFile.getModelBlock()
                    .getModelItems()
                    .get(0)
                    .getDeclaredVariables();
            Assertions.assertEquals(1, variables.size());
            Assertions.assertEquals("variableA", variables.get(0).getName());
        });
    }
}
