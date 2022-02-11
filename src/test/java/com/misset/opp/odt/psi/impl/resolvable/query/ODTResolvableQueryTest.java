package com.misset.opp.odt.psi.impl.resolvable.query;

import com.intellij.codeInspection.LocalInspectionTool;
import com.misset.opp.odt.inspection.resolvable.ODTResolvableInspection;
import com.misset.opp.testCase.OMTInspectionTestCase;
import com.misset.opp.testCase.OMTOntologyTestCase;
import com.misset.opp.ttl.util.TTLValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;

class ODTResolvableQueryTest extends OMTInspectionTestCase {

    @Override
    protected Collection<Class<? extends LocalInspectionTool>> getEnabledInspections() {
        return Collections.singleton(ODTResolvableInspection.class);
    }

    @BeforeEach
    public void setUp() {
        super.setUp();
        OMTOntologyTestCase.initOntologyModel();
    }

    @Test
    void testHasErrorOnNonBooleanFilterContent() {
        configureByText(insideProcedureRunWithPrefixes("/ont:ClassA / ^rdf:type[rdf:type]"));
        assertHasError(TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorOnBooleanFilterContent() {
        configureByText(insideProcedureRunWithPrefixes("/ont:ClassA / ^rdf:type[rdf:type == /ont:ClassA]"));
        assertNoError(TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorOnOwlThingFilterContent() {
        configureByText(insideProcedureRunWithPrefixes("/owl:Thing / ^rdf:type[rdf:type == /ont:ClassA]"));
        assertNoError(TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorOnEmptyFilterContent() {
        configureByText(insideProcedureRunWithPrefixes("$unknownTypeVAriable / ^rdf:type[rdf:type == /ont:ClassA]"));
        assertNoError(TTLValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }
}
