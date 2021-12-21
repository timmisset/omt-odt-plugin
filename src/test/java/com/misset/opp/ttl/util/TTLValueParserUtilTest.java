package com.misset.opp.ttl.util;

import com.misset.opp.testCase.OMTOntologyTestCase;
import org.apache.jena.ontology.OntResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TTLValueParserUtilTest {
    @BeforeEach
    protected void setUp() {
        OMTOntologyTestCase.initOntologyModel();
    }

    @Test
    void parseValue() {
        final OntResource ontResource = TTLValueParserUtil.parseValue("12");
    }
}
