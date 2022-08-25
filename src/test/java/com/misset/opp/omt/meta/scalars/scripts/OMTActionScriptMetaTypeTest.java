package com.misset.opp.omt.meta.scalars.scripts;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.odt.inspection.ODTCodeInspectionUnresolvableReference;
import com.misset.opp.odt.inspection.calls.ODTCallInspection;
import com.misset.opp.omt.inspection.structure.OMTValueInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Set;

class OMTActionScriptMetaTypeTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Set.of(OMTValueInspection.class, ODTCallInspection.class, ODTCodeInspectionUnresolvableReference.class);
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
        assertNoErrors();
    }

    @Test
    void testOnSelectProvidesNoValueWithoutDynamicActionQuery() {
        String content = "" +
                "actions:\n" +
                "   notifications:\n" +
                "   -   onSelect: |\n" +
                "           @LOG($value);";
        configureByText("my.module.omt", content);
        assertHasError("Cannot resolve variable '$value'");
    }

}
