package com.misset.opp.omt.meta.scalars;

import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static com.misset.opp.omt.meta.scalars.OMTVariableNameMetaType.SYNTAX_ERROR;

class OMTVariableNameTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(OMTValueInspection.class));
    }

    @Test
    void validateScalarValueErrorWhenNoName() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   name: $\n" +
                "";
        configureByText(content);
        inspection.assertHasError(SYNTAX_ERROR);
    }

    @Test
    void validateScalarValueErrorWhenNoPrefix() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   name: param\n" +
                "";
        configureByText(content);
        inspection.assertHasError(SYNTAX_ERROR);
    }

    @Test
    void validateScalarValueNoError() {

        String content = "model:\n" +
                "   Activiteit: !Activity\n" +
                "       params:\n" +
                "       -   name: $param\n" +
                "";
        configureByText(content);
        inspection.assertNoErrors();
    }
}
