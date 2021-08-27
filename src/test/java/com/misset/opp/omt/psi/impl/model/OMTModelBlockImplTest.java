package com.misset.opp.omt.psi.impl.model;

import com.intellij.openapi.application.ReadAction;
import com.misset.opp.omt.psi.OMTModelItemType;
import com.misset.opp.omt.psi.model.modelitems.OMTModelItem;
import com.misset.opp.testCase.OMTTestCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class OMTModelBlockImplTest extends OMTTestCase {

    @BeforeEach
    protected void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void getSingleModelItem() {
        configureByText(insideActivityWithPrefixes("onStart: \n"));
        final List<OMTModelItem> modelItems = ReadAction.compute(() -> getOMTFile().getModelBlock()
                .getModelItems());
        Assertions.assertEquals(1, modelItems.size());
        Assertions.assertFalse(modelItems.get(0).isEmpty());
        Assertions.assertEquals(OMTModelItemType.Activity, modelItems.get(0).getType());
    }

    @Test
    void getSingleEmptyModelItem() {
        configureByText(insideActivityWithPrefixes(""));
        final List<OMTModelItem> modelItems = ReadAction.compute(() -> getOMTFile().getModelBlock()
                .getModelItems());
        Assertions.assertEquals(1, modelItems.size());
        Assertions.assertTrue(modelItems.get(0).isEmpty());
        Assertions.assertEquals(OMTModelItemType.Activity, modelItems.get(0).getType());
    }

    @Test
    void getMultipleModelItems() {
        configureByText("model:\n" +
                "   Activiteit: !Activity\n" +
                "       onStart: \n" +
                "   Procedure: !Procedure\n" +
                "       onRun: \n");
        final List<OMTModelItem> modelItems = ReadAction.compute(() -> getOMTFile().getModelBlock()
                .getModelItems());
        Assertions.assertEquals(2, modelItems.size());
    }
}
