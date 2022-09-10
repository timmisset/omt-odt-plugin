package com.misset.opp.omt.meta;

import com.misset.opp.omt.inspection.structure.OMTUnkownKeysInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OMTActionsMapMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Set.of(OMTUnkownKeysInspection.class, OMTValueInspection.class));
    }

    @Test
    void testNoErrorsOnActionsMap() {
        configureByText(insideActivityWithPrefixes(
                "actions: \n" +
                        "   myAction:\n" +
                        "       onSelect: |\n" +
                        "           @LOG('do something');\n" +
                        "       disabled: true\n" +
                        ""
        ));
        inspection.assertNoErrors();
    }

    @Test
    void testNoIdForMappedAction() {
        configureByText(insideActivityWithPrefixes(
                "actions: \n" +
                        "   myAction:\n" +
                        "       id: id\n"
        ));
        inspection.assertHasWarning("Unnecessary id field when action is mapped to a key");
    }

}
