package com.misset.opp.odt.inspection.resolvable;

import com.misset.opp.model.util.OntologyValidationUtil;
import com.misset.opp.odt.testcase.ODTTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class ODTResolvableInspectionTest extends ODTTestCase {

    @BeforeEach
    public void setUp() {
        super.setUp();
        myFixture.enableInspections(Collections.singleton(ODTResolvableInspection.class));
    }

    @Test
    void testHasErrorOnNonBooleanFilterContent() {
        configureByText(withPrefixes("/ont:ClassA / ^rdf:type[rdf:type]"));
        inspection.assertHasError(OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorOnBooleanFilterContent() {
        configureByText(withPrefixes("/ont:ClassA / ^rdf:type[rdf:type == /ont:ClassA]"));
        inspection.assertNoError(OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorOnOwlThingFilterContent() {
        configureByText(withPrefixes("/owl:Thing / ^rdf:type[rdf:type == /ont:ClassA]"));
        inspection.assertNoError(OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }

    @Test
    void testHasNoErrorOnEmptyFilterContent() {
        configureByText(withPrefixes("$unknownTypeVAriable / ^rdf:type[rdf:type == /ont:ClassA]"));
        inspection.assertNoError(OntologyValidationUtil.ERROR_MESSAGE_BOOLEAN);
    }
}
