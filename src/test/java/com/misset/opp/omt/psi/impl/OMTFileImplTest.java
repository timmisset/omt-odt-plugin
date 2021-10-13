package com.misset.opp.omt.psi.impl;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.omt.psi.OMTFile;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OMTFileImplTest extends OMTTestCase {

    @Test
    void getModelBlock() {
        configureByText(insideActivityWithPrefixes(""));
        ReadAction.run(() -> Assertions.assertNotNull(getOMTFile().getModelBlock()));
    }

    @Test
    void getPrefixBlock() {
        configureByText(insideActivityWithPrefixes(""));
        ReadAction.run(() -> Assertions.assertNotNull(getOMTFile().getModelBlock()));
    }
}
