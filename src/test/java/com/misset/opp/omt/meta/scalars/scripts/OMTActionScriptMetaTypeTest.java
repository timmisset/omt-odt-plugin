package com.misset.opp.omt.meta.scalars.scripts;

import com.misset.opp.odt.inspection.ODTCodeInspectionUnresolvableReference;
import com.misset.opp.odt.inspection.calls.ODTCallInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.omt.testcase.OMTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

class OMTActionScriptMetaTypeTest extends OMTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Set.of(OMTValueInspection.class, ODTCallInspection.class, ODTCodeInspectionUnresolvableReference.class));
    }

    @Test
    void testOnSelectProvidesValue() {
        String content = "" +
                "actions:\n" +
                "   notifications:\n" +
                "   -   dynamicActionQuery: |\n" +
                "           'A' | 'B'\n" +
                "       onSelect: |\n" +
                "           @LOG($value);";
        configureByText("my.module.omt", content);
        inspection.assertNoErrors();
    }

    @Test
    void testOnSelectProvidesNoValueWithoutDynamicActionQuery() {
        String content = "" +
                "actions:\n" +
                "   notifications:\n" +
                "   -   onSelect: |\n" +
                "           @LOG($value);";
        configureByText("my.module.omt", content);
        inspection.assertHasError("Cannot resolve variable '$value'");
    }

}
